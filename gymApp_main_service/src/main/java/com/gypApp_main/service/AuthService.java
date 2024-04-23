package com.gypApp_main.service;

import com.gypApp_main.dto.ChangeLoginRequest;
import com.gypApp_main.dto.JwtResponse;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import com.gypApp_main.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final JWTProvider provider;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder encoder;

    public JwtResponse login(LoginRequest request) {
        if (loginAttemptService.isBlocked(request.getUsername())) {
            log.warn("Login attempt blocked for user: {}", request.getUsername());
            throw new BadCredentialsException("Your account is temporarily blocked. Please try again later.");
        }

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(request.getUsername());
        } catch (UsernameNotFoundException ex) {
            loginAttemptService.loginFailed(request.getUsername());
            log.error("UsernameNotFoundException occurred for username: {}", request.getUsername(), ex);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (decoderPassword(request.getPassword(), userDetails.getPassword())) {
            loginAttemptService.loginSucceeded(request.getUsername());
            String token = provider.generateToken(userDetails);
            log.info("User logged in successfully: {}", request.getUsername());
            return new JwtResponse(token);
        } else {
            loginAttemptService.loginFailed(request.getUsername());
            log.warn("Login attempt failed for user: {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    public HttpStatus changePassword(ChangeLoginRequest request) {
        User user = userService.findUserByUserName(request.getUsername());
        if (decoderPassword(request.getOldPassword(), user.getPassword())) {
            userService.changePassword(request.getUsername(), request.getNewPassword());
            log.info("Password changed successfully for user: {}", request.getUsername());
            return HttpStatus.OK;
        } else {
            log.warn("Failed to change password for user: {}", request.getUsername());
            return HttpStatus.BAD_REQUEST;
        }
    }

    public String createTrainee(Trainee trainee) {
        String password = userService.generatePassword();
        trainee.getUser().setPassword(encoderPassword(password));
        Trainee createdTrainee = traineeService.createTrainee(trainee, trainee.getUser());
        log.info("Trainee created successfully with username: {}", createdTrainee.getUser().getUserName());
        log.debug("Trainee password: {}", password);

        return "Username :" + createdTrainee.getUser().getUserName() + " Password :" + password;
    }

    public String createTrainer(Trainer trainer) {
        String password = userService.generatePassword();
        trainer.getUser().setPassword(encoderPassword(password));
        Trainer createdTrainer = trainerService.createTrainer(trainer, trainer.getUser(), trainer.getSpecialization().getTrainingTypeName());
        log.info("Trainer created successfully with username: {}", createdTrainer.getUser().getUserName());
        log.debug("Trainer password: {}", password);

        return "Username :" + createdTrainer.getUser().getUserName() + " Password :" + password;
    }

    private String encoderPassword(String password) {
        log.debug("Encrypting password");
        return encoder.encode(password);
    }

    private boolean decoderPassword(String rawPassword, String encoderPassword) {
        log.debug("Decrypting password");
        return encoder.matches(rawPassword, encoderPassword);
    }
}
