package com.gypApp_main.serviceTest;

import com.gypApp_main.dao.TraineeDAO;
import com.gypApp_main.dao.TrainerDAO;
import com.gypApp_main.dao.UserDAO;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.model.User;
import com.gypApp_main.service.TrainerService;
import com.gypApp_main.service.TrainingTypeService;
import com.gypApp_main.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserService userService;


    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        String trainingTypeName = "Cardio";

        when(userService.createUser(any(User.class))).thenReturn(new User());
        when(trainingTypeService.findByTrainingName(trainingTypeName)).thenReturn(new TrainingType());
        when(trainerDAO.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.createTrainer(trainer, user, trainingTypeName);

        assertNotNull(result);
        verify(userService, times(1)).createUser(user);
        verify(trainingTypeService, times(1)).findByTrainingName(trainingTypeName);
        verify(trainerDAO, times(1)).save(any(Trainer.class));
    }

    @Test
    void testSelectTrainerByUserName() {
        String username = "testTrainer";
        when(trainerDAO.findTrainerByUserUserName(username)).thenReturn(Optional.of(new Trainer()));

        Trainer selectedTrainer = trainerService.selectTrainerByUserName(username);

        assertNotNull(selectedTrainer);

        verify(trainerDAO, times(1)).findTrainerByUserUserName(username);
    }

    @Test
    void testGetNotAssignedActiveTrainers() {
        String username = "testUser";
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer(), new Trainer());

        when(trainerDAO.getNotAssignedActiveTrainers(username)).thenReturn(trainers);

        List<Trainer> result = trainerService.getNotAssignedActiveTrainers(username);

        assertNotNull(result);
        assertEquals(trainers.size(), result.size());
        verify(trainerDAO, times(1)).getNotAssignedActiveTrainers(username);
    }


    @Test
    public void testUpdateTrainer() {
        // Arrange
        String username = "testUsername";
        Trainer existingTrainee = new Trainer();
        existingTrainee.setId(1L);

        Trainer updatedTrainee = new Trainer();

        User existingUser = new User();
        existingUser.setId(1L);
        existingTrainee.setUser(existingUser);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setUserName("newUsername");

        updatedTrainee.setUser(newUser);

        when(trainerDAO.findTrainerByUserUserName(username)).thenReturn(Optional.of(existingTrainee));
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(newUser);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(existingTrainee);

        Trainer result = trainerService.updateTrainer(username, updatedTrainee);

        assertNotNull(result);
        assertEquals(newUser, result.getUser());
        verify(trainerDAO, times(1)).findTrainerByUserUserName(username);
        verify(userService, times(1)).updateUser(anyString(), any(User.class));
        verify(trainerDAO, times(1)).save(any(Trainer.class));
    }

    @Test
    void testChangePassword_Success() {
        String username = "testUser";

        String newPassword = "newPassword";
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        when(trainerDAO.findTrainerByUserUserName(username)).thenReturn(Optional.of(trainer));
        when(userService.changePassword(username, newPassword)).thenReturn("encryptedPassword");

        trainerService.changePassword(username, newPassword);

        assertEquals("encryptedPassword", trainer.getUser().getPassword());
        verify(trainerDAO, times(1)).findTrainerByUserUserName(username);
        verify(userService, times(1)).changePassword(username, newPassword);
    }

    @Test
    void testChangeStatus_Success() {
        String username = "testUser";
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        when(trainerDAO.findTrainerByUserUserName(username)).thenReturn(Optional.of(trainer));
        when(userService.changeStatus(username)).thenReturn(true);

        trainerService.changeStatus(username);

        assertTrue(trainer.getUser().getIsActive());
        verify(trainerDAO, times(1)).findTrainerByUserUserName(username);
        verify(userService, times(1)).changeStatus(username);
    }


}