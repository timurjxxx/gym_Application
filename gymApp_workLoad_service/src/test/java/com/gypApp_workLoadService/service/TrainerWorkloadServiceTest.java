package com.gypApp_workLoadService.service;

import com.gypApp_workLoadService.dao.TrainerWorkloadDAO;
import com.gypApp_workLoadService.dto.ActionType;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadDAO trainerWorkloadDAO;

    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateWorkload_NewTrainer() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .type(ActionType.ADD)
                .trainingDuration(60)
                .trainingDate(LocalDate.now())
                .build();

        when(trainerWorkloadDAO.findTrainerByUsername("john_doe")).thenReturn(null);

        trainerWorkloadService.updateWorkload(request);

        verify(trainerWorkloadDAO, times(1)).save(any(Trainer.class));
    }

    @Test
    public void testUpdateWorkload_ExistingTrainer() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .type(ActionType.ADD)
                .trainingDuration(60)
                .trainingDate(LocalDate.now())
                .build();

        Map<String, Integer> trainingSummary = new HashMap<>();
        trainingSummary.put("4:2022", 120); // Example existing data
        Trainer existingTrainer = Trainer.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingSummaryDuration(trainingSummary)
                .build();

        when(trainerWorkloadDAO.findTrainerByUsername("john_doe")).thenReturn(existingTrainer);

        trainerWorkloadService.updateWorkload(request);

        verify(trainerWorkloadDAO, times(1)).save(any(Trainer.class));
    }



    @Test
    public void testCreateNewTrainer() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .type(ActionType.ADD)
                .trainingDuration(60)
                .trainingDate(LocalDate.now())
                .build();

        Trainer newTrainer = trainerWorkloadService.createNewTrainer(request);

        assertNotNull(newTrainer);
        assertEquals("john_doe", newTrainer.getUsername());
        assertEquals("John", newTrainer.getFirstName());
        assertEquals("Doe", newTrainer.getLastName());
        assertTrue(newTrainer.getIsActive());

        assertEquals(1, newTrainer.getTrainingSummaryDuration().size());
        assertTrue(newTrainer.getTrainingSummaryDuration().containsKey(LocalDate.now().getMonthValue() + ":" + LocalDate.now().getYear()));
        assertEquals(60, newTrainer.getTrainingSummaryDuration().get(LocalDate.now().getMonthValue() + ":" + LocalDate.now().getYear()));
    }


    @Test
    public void testGetYearAndMonth() {
        Trainer existingTrainer = Trainer.builder()
                .trainingSummaryDuration(new HashMap<>())
                .build();
        existingTrainer.getTrainingSummaryDuration().put("4:2022", 120);

        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainingDate(LocalDate.of(2022, 4, 15))
                .build();

        String yearAndMonth = trainerWorkloadService.getYearAndMonth(existingTrainer.getTrainingSummaryDuration(), request);

        assertEquals("4:2022", yearAndMonth);
    }

    @Test
    public void testGenerateKeyForTrainingSummaryDuration() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainingDate(LocalDate.of(2022, 4, 15))
                .build();

        String key = trainerWorkloadService.generateKeyForTrainingSummaryDuration(request);

        assertEquals("4:2022", key);
    }

    @Test
    void testUpdateTrainerWorkLoad_AddExistingRecord() {
        Map<String, Integer> trainingSummary = new HashMap<>();
        trainingSummary.put("4:2022", 120);
        Trainer trainer = Trainer.builder()
                .trainingSummaryDuration(trainingSummary)
                .build();
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .type(ActionType.ADD)
                .trainingDuration(60)
                .trainingDate(LocalDate.of(2022, 4, 1))
                .build();

        Trainer updatedTrainer = trainerWorkloadService.updateTrainerWorkLoad(trainer, request);


        assertEquals(1, updatedTrainer.getTrainingSummaryDuration().size());
        assertTrue(updatedTrainer.getTrainingSummaryDuration().containsKey("4:2022"));
        assertEquals(180, updatedTrainer.getTrainingSummaryDuration().get("4:2022"));
    }

    @Test
    void testUpdateTrainerWorkLoad_RemoveExistingRecord() {
        Map<String, Integer> trainingSummary = new HashMap<>();
        trainingSummary.put("4:2022", 120);
        Trainer trainer = Trainer.builder()
                .trainingSummaryDuration(trainingSummary)
                .build();
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .type(ActionType.DELETE)
                .trainingDuration(60)
                .trainingDate(LocalDate.of(2022, 4, 1))
                .build();

        Trainer updatedTrainer = trainerWorkloadService.updateTrainerWorkLoad(trainer, request);

        assertEquals(1, updatedTrainer.getTrainingSummaryDuration().size());
        assertTrue(updatedTrainer.getTrainingSummaryDuration().containsKey("4:2022"));
        assertEquals(60, updatedTrainer.getTrainingSummaryDuration().get("4:2022"));
    }

    @Test
    void testUpdateTrainerWorkLoad_AddNewRecord() {
        Trainer trainer = Trainer.builder()
                .trainingSummaryDuration(new HashMap<>())
                .build();
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .type(ActionType.ADD)
                .trainingDuration(60)
                .trainingDate(LocalDate.of(2022, 4, 1))
                .build();

        Trainer updatedTrainer = trainerWorkloadService.updateTrainerWorkLoad(trainer, request);

        assertEquals(1, updatedTrainer.getTrainingSummaryDuration().size());
        assertTrue(updatedTrainer.getTrainingSummaryDuration().containsKey("4:2022"));
        assertEquals(60, updatedTrainer.getTrainingSummaryDuration().get("4:2022"));
    }

}
