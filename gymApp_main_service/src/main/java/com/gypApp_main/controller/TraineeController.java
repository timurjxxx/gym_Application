package com.gypApp_main.controller;

import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.service.TraineeService;
import com.gypApp_main.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/trainee", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TraineeController(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @GetMapping("/get_Trainee/{username}")
    public ResponseEntity<String> getTraineeProfile(@PathVariable("username") String username   ) {
        log.info("Fetching trainee profile for username: {}", username);
        Trainee trainee = traineeService.selectTraineeByUserName(username);
        log.debug("Trainee details: {}", trainee);
        return ResponseEntity.ok(trainee + trainee.getTrainers().toString());
    }

    @PutMapping(value = "/update_Trainee/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTraineeProfile(@PathVariable("username") String username, @RequestBody Trainee trainee) {
        log.info("Updating trainee profile for username: {}", username);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee.getUser().getUserName(), trainee);
        log.debug("Updated trainee details: {}", updatedTrainee);
        return ResponseEntity.ok(updatedTrainee.toString() + updatedTrainee.getTrainers().toString());
    }

    @DeleteMapping("/delete_Trainee/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable("username") String username) {
        log.info("Deleting trainee profile for username: {}", username);
        traineeService.deleteTraineeByUserName(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/updateTrainersList/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTraineeTrainersList(@PathVariable("username") String username, @RequestBody Map<String, Object> jsonData) {
        log.info("Updating trainers list for trainee with username: {}", username);
        String traineeUsername = (String) jsonData.get("traineeUsername");
        List<String> trainerUsernames = (List<String>) jsonData.get("trainerUsernames");
        Set<Trainer> trainers = new HashSet<>();
        for (String item : trainerUsernames) {
            trainers.add(trainerService.selectTrainerByUserName(item));
        }
        Trainee updatedTrainee = traineeService.updateTraineeTrainersList(traineeUsername, trainers);
        log.debug("Updated trainee details with new trainers: {}", updatedTrainee);
        return ResponseEntity.ok(updatedTrainee.getTrainers().toString());
    }

    @PatchMapping(value = "/change_status/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> activateDeactivateTrainee(@PathVariable("username") String username) {
        log.info("Activating/deactivating trainee with username: {}", username);
        traineeService.changeStatus(username);
        return ResponseEntity.ok().build();
    }
}
