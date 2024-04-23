package com.gypApp_main.service;

import com.gypApp_main.dao.TraineeDAO;
import com.gypApp_main.exception.UserNotFoundException;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
@Slf4j
public class TraineeService {

    private final UserService userService;

    private final TraineeDAO traineeDAO;


    @Autowired
    public TraineeService(TraineeDAO traineeDAO, UserService userService) {
        this.traineeDAO = traineeDAO;
        this.userService = userService;
    }

    @Transactional
    public Trainee createTrainee(@Valid Trainee trainee, @NotNull User user) {
        log.info("Create trainee with cred {}, {}", trainee, user);
        log.debug("User credentials {}", user);
        trainee.setUser(userService.createUser(user));
        return traineeDAO.save(trainee);
    }

    @Transactional(readOnly = true)
    public Trainee selectTraineeByUserName(@NotBlank String username) throws UserNotFoundException {
        log.info("Get trainee profile by username {}", username);
        return traineeDAO.findTraineeByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));
    }

    @Transactional
    public Trainee updateTrainee(@NotBlank String username, @Valid Trainee updatedTrainee) throws UserNotFoundException {
        log.info("Update trainee with username {}", username);
        log.info("UpdatedTrainee credentials {}", updatedTrainee);
        Trainee trainee = traineeDAO.findTraineeByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));
        trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
        trainee.setAddress(updatedTrainee.getAddress());
        trainee.setUser(userService.updateUser(updatedTrainee.getUser().getUserName(), updatedTrainee.getUser()));
        log.info("Saved updatedTrainee {}", trainee);
        log.debug("Saved trainee credentials {}, {}", trainee, trainee.getUser());
        return traineeDAO.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUserName(@NotBlank String username) throws UserNotFoundException {
        traineeDAO.findTraineeByUserUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));

        traineeDAO.deleteTraineeByUserUserName(username);
        log.info("Deleted trainee with username: {}", username);
        log.debug("Deleted trainee with username {}  ", username);
    }

    @Transactional
    public Trainee updateTraineeTrainersList(@NotBlank String username, @NotBlank Set<Trainer> updatedList) throws UserNotFoundException {
        Trainee trainee = traineeDAO.findTraineeByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));
        trainee.setTrainers(updatedList);
        log.info("Updated trainers list for trainee with username {}", username);
        log.debug("Updated trainee details: new trainers list {}", updatedList);

        return traineeDAO.save(trainee);
    }

    @Transactional
    public void changePassword(@NotBlank String username, @NotBlank String newPassword) throws UserNotFoundException {
        Trainee trainee = traineeDAO.findTraineeByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));
        trainee.getUser().setPassword(userService.changePassword(username, newPassword));
        log.info("Changed password for trainee with username: {}", username);
    }

    @Transactional
    public void changeStatus(@NotBlank String username) throws UserNotFoundException {
        Trainee trainee = traineeDAO.findTraineeByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainee with username: " + username + " is not found"));
        trainee.getUser().setIsActive(userService.changeStatus(username));
        log.info("Changed status for trainee with ID: {}", trainee.getId());
    }
}