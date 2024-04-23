package com.gypApp_main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gypApp_main.model.Training;
import com.gypApp_main.model.TrainingSearchCriteria;
import com.gypApp_main.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/training", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping(value = "/create_training", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTraining(@RequestBody Training training) throws JsonProcessingException {
        String trainerName = training.getTrainer().getUser().getUserName();
        String traineeName = training.getTrainee().getUser().getUserName();
        String trainingTypeName = training.getTrainingTypes().getTrainingTypeName();
        log.info("Creating training with trainer: {}, trainee: {}, training type: {}", trainerName, traineeName, trainingTypeName);
        Training training1 = trainingService.addTraining(training, trainerName, traineeName, trainingTypeName);
        log.debug("Training created successfully");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteTraining(@PathVariable String name) throws JsonProcessingException {
        
        trainingService.deleteTraining(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/trainee/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTraineeTrainingsByCriteria(@PathVariable("username") String username, @RequestBody TrainingSearchCriteria criteria) {
        log.info("Fetching trainee trainings for username: {} with criteria: {}", username, criteria);
        List<Training> trainings = trainingService.getTraineeTrainingsByCriteria(username, criteria);
        log.debug("Fetched trainee trainings: {}", trainings);
        return ResponseEntity.ok(trainings.toString());
    }

    @GetMapping(value = "/trainer/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTrainerTrainingsByCriteria(@PathVariable("username") String username, @RequestBody TrainingSearchCriteria criteria) {
        log.info("Fetching trainer trainings for username: {} with criteria: {}", username, criteria);
        List<Training> trainings = trainingService.getTrainerTrainingsByCriteria(username, criteria);
        log.debug("Fetched trainer trainings: {}", trainings);
        return ResponseEntity.ok(trainings.toString());
    }
}
