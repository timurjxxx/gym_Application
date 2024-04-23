package com.gypApp_main.controllerTest;

import com.gypApp_main.controller.TraineeController;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import com.gypApp_main.service.TraineeService;
import com.gypApp_main.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getTraineeProfile() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        when(traineeService.selectTraineeByUserName(username)).thenReturn(trainee);

        ResponseEntity<String> response = traineeController.getTraineeProfile(username);

        assertEquals(ResponseEntity.ok(trainee + trainee.getTrainers().toString()), response);
    }

    @Test
    public void testUpdateTraineeProfile() {

        Trainee trainer = new Trainee();
        trainer.setUser(new User());
        Set<Trainer> set = new HashSet<>();
        trainer.setTrainers(set);

        when(traineeService.updateTrainee(Mockito.any(), Mockito.any())).thenReturn(trainer);

        ResponseEntity<String> response = traineeController.updateTraineeProfile("username", trainer);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trainer.toString() + trainer.getTrainers().toString(), response.getBody());
    }

    @Test
    void deleteTraineeProfile() {
        String username = "testUser";

        ResponseEntity<Void> response = traineeController.deleteTraineeProfile(username);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(traineeService, times(1)).deleteTraineeByUserName(username);
    }

    @Test
    void updateTraineeTrainersList_shouldReturnUpdatedTrainee() {
        String username = "traineeUsername";
        String traineeUsername = "traineeUsername";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("traineeUsername", traineeUsername);
        jsonData.put("trainerUsernames", trainerUsernames);

        Set<Trainer> trainers = new HashSet<>();
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        trainers.add(trainer1);
        trainers.add(trainer2);

        when(trainerService.selectTrainerByUserName("trainer1")).thenReturn(trainer1);
        when(trainerService.selectTrainerByUserName("trainer2")).thenReturn(trainer2);

        Trainee updatedTrainee = new Trainee();
        when(traineeService.updateTraineeTrainersList(traineeUsername, trainers)).thenReturn(updatedTrainee);

        ResponseEntity<String> response = traineeController.updateTraineeTrainersList(username, jsonData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTrainee.getTrainers().toString(), response.getBody());

        verify(trainerService, times(2)).selectTrainerByUserName(anyString());
        verify(traineeService, times(1)).updateTraineeTrainersList(traineeUsername, trainers);
    }


    @Test
    void activateDeactivateTrainee() {
        String username = "testUser";

        ResponseEntity<Void> response = traineeController.activateDeactivateTrainee(username);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(traineeService, times(1)).changeStatus(username);
    }


}