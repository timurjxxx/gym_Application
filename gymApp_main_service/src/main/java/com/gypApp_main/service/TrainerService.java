package com.gypApp_main.service;

import com.gypApp_main.dao.TrainerDAO;
import com.gypApp_main.exception.UserNotFoundException;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Service
@Slf4j
public class TrainerService {

    private final TrainerDAO trainerDAO;
    private final UserService userService;

    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO, UserService userService, TrainingTypeService trainingTypeService) {
        this.trainerDAO = trainerDAO;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }

    @Transactional
    public Trainer createTrainer(@Valid Trainer trainer, @Valid User user, @NotBlank String trainingTypeName) {
        log.info("Create trainer with cred {}, {}", trainer, user);
        log.debug("User credentials {}", user);
        trainer.setUser(userService.createUser(user));
        trainer.setSpecialization(trainingTypeService.findByTrainingName(trainingTypeName));
        return trainerDAO.save(trainer);
    }

    @Transactional(readOnly = true)
    public Trainer selectTrainerByUserName(@NotBlank String username) throws UserNotFoundException {
        log.info("Find trainer with username: {}", username);
        log.debug("Trainer username: {}", username);
        return trainerDAO.findTrainerByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainer with username " + username + " is not found"));
    }

    @Transactional
    public Trainer updateTrainer(@NotBlank String username, @Valid Trainer updatedTrainer) throws UserNotFoundException {
        log.info("Update trainer with username {}", username);
        log.info("UpdatedTrainer credentials {}", updatedTrainer);
        Trainer trainer = trainerDAO.findTrainerByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainer with username " + username + " is not found"));
        trainer.setUser(userService.updateUser(updatedTrainer.getUser().getUserName(), updatedTrainer.getUser()));
        log.info("Saved updatedTrainer {}", trainer);
        log.debug("Saved trainer credentials {}, {}", trainer, trainer.getUser());
        return trainerDAO.save(trainer);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getNotAssignedActiveTrainers(@NotBlank String username) throws UserNotFoundException {
        log.info("Find trainee with username: {}", username);
        return trainerDAO.getNotAssignedActiveTrainers(username);
    }

    @Transactional
    public void changeStatus(@NotBlank String username) throws UserNotFoundException {
        Trainer trainer = trainerDAO.findTrainerByUserUserName(username).orElseThrow(() -> new UserNotFoundException("Trainer with username " + username + " is not found"));
        trainer.getUser().setIsActive(userService.changeStatus(username));
        log.info("Changed status for trainer username: {}", username);
    }

    @Transactional
    public void changePassword(@NotBlank String userName, @NotBlank String newPassword) throws UserNotFoundException {
        Trainer trainer = trainerDAO.findTrainerByUserUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Trainer with username " + userName + " is not found"));
        trainer.getUser().setPassword(userService.changePassword(userName, newPassword));
        trainerDAO.save(trainer);

        log.info("Changed password for trainer with username: {}", userName);
        log.debug("Updated trainer details: new password is {}", newPassword);
    }
}