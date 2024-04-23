package com.gypApp_main.serviceTest;

import com.gypApp_main.dto.ChangeLoginRequest;
import com.gypApp_main.dto.JwtResponse;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.model.User;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private JWTProvider provider;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        // Mocking
        LoginRequest request = new LoginRequest("username", "password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("username", "password", Collections.emptyList());
        when(userService.loadUserByUsername("username")).thenReturn(userDetails);
        when(encoder.matches("password", userDetails.getPassword())).thenReturn(true);
        when(provider.generateToken(userDetails)).thenReturn("token");

        // Test
        JwtResponse response = authService.login(request);

        // Verify
        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(loginAttemptService, times(1)).loginSucceeded("username");
    }



    @Test
    void testCreateTrainee() {
        // Arrange
        Trainee trainee = mock(Trainee.class);
        User user = mock(User.class);
        when(trainee.getUser()).thenReturn(user);
        when(traineeService.createTrainee(any(), any())).thenReturn(trainee);

        // Act
        String result = authService.createTrainee(trainee);

        // Assert
        assertEquals("Username :null Password :null", result);
        verify(traineeService).createTrainee(trainee, user);
    }

    @Test
    void testCreateTrainer() {
        // Arrange
        Trainer trainer = mock(Trainer.class);
        User user = mock(User.class);
        when(trainer.getUser()).thenReturn(user);
        when(trainer.getSpecialization()).thenReturn(mock(TrainingType.class));
        when(trainerService.createTrainer(any(), any(), any())).thenReturn(trainer);

        // Act
        String result = authService.createTrainer(trainer);

        // Assert
        assertEquals("Username :null Password :null", result);
        verify(trainerService).createTrainer(trainer, user, trainer.getSpecialization().getTrainingTypeName());
    }



  @Test
    public void testChangePassword_Success() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest("username", "oldPassword", "newPassword");
        User user = new User();
        user.setPassword("encodedOldPassword");
        when(userService.findUserByUserName(request.getUsername())).thenReturn(user);
        when(encoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);

        // Act
        HttpStatus status = authService.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, status);
        verify(userService, times(1)).changePassword(request.getUsername(), request.getNewPassword());
    }



    @Test
    public void testLogin_UserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest("nonExistingUser", "password");

        when(loginAttemptService.isBlocked("nonExistingUser")).thenReturn(false);
        when(userService.loadUserByUsername("nonExistingUser")).thenThrow(new UsernameNotFoundException("User not found"));

        // Act and Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        verify(loginAttemptService, times(1)).loginFailed("nonExistingUser");
    }
    @Test
    public void testLogin_IncorrectPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("existingUser", "incorrectPassword");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("existingUser", "hashedPassword", Collections.emptyList());

        when(loginAttemptService.isBlocked("existingUser")).thenReturn(false);
        when(userService.loadUserByUsername("existingUser")).thenReturn(userDetails);
        when(encoder.matches("incorrectPassword", userDetails.getPassword())).thenReturn(false);

        // Act and Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        verify(loginAttemptService, times(1)).loginFailed("existingUser");
    }


    @Test
    public void testLogin_UserBlocked() {
        // Arrange
        LoginRequest request = new LoginRequest("blockedUser", "password");

        when(loginAttemptService.isBlocked("blockedUser")).thenReturn(true);

        // Act and Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });

        assertEquals("Your account is temporarily blocked. Please try again later.", exception.getMessage());
        verify(loginAttemptService, never()).loginFailed("blockedUser");
    }

}
