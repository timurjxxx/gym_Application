package com.gypApp_main.serviceTest;

import com.gypApp_main.dao.TraineeDAO;
import com.gypApp_main.dao.TrainerDAO;
import com.gypApp_main.dao.UserDAO;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import com.gypApp_main.service.TraineeService;
import com.gypApp_main.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class TraineeServiceTest {

    @Mock
    private UserService userService;


    @Mock
    private UserDAO userDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;


    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("name");
        user.setLastName("last name");
        user.setIsActive(true);
        when(userService.createUser(user)).thenReturn(user);

        when(traineeDAO.save(trainee)).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee, user);

        assertNotNull(createdTrainee);
        assertEquals(user, trainee.getUser());
        assertEquals("name", createdTrainee.getUser().getFirstName());

        verify(userService, times(1)).createUser(user);
        verify(traineeDAO, times(1)).save(trainee);
    }

    @Test
    void testSelectTraineeByUserName() {
        String username = "testTrainee";
        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(new Trainee()));

        Trainee selectedTrainee = traineeService.selectTraineeByUserName(username);

        assertNotNull(selectedTrainee);

        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
    }



    @Test
    public void testUpdateTrainee() {
        // Arrange
        String username = "testUsername";
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setDateOfBirth(LocalDate.now());
        updatedTrainee.setAddress("New Address");

        User existingUser = new User();
        existingUser.setId(1L);
        existingTrainee.setUser(existingUser);

        // Create a new user for the updatedTrainee
        User newUser = new User();
        newUser.setId(2L);
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setUserName("newUsername");

        updatedTrainee.setUser(newUser);

        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(existingTrainee));
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(newUser);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(existingTrainee);

        // Act
        Trainee result = traineeService.updateTrainee(username, updatedTrainee);

        // Assert
        assertNotNull(result);
        assertEquals(updatedTrainee.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatedTrainee.getAddress(), result.getAddress());
        assertEquals(newUser, result.getUser()); // Assert the updated user
        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
        verify(userService, times(1)).updateUser(anyString(), any(User.class));
        verify(traineeDAO, times(1)).save(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeTrainersList() {
        String username = "john.doe";

        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);

        Set<Trainer> updatedList = new HashSet<>();
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        updatedList.add(trainer1);
        updatedList.add(trainer2);

        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(existingTrainee));
        when(traineeDAO.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee savedTrainee = invocation.getArgument(0);
            return savedTrainee;
        });

        Trainee result = traineeService.updateTraineeTrainersList(username, updatedList);

        assertNotNull(result);
        assertEquals(updatedList, result.getTrainers());

        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
        verify(traineeDAO, times(1)).save(any(Trainee.class));
    }

    @Test
    void testChangePassword_Success() {
        String username = "testUser";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(trainee));
        when(userService.changePassword(username, newPassword)).thenReturn("encryptedPassword");

        traineeService.changePassword(username, newPassword);

        assertEquals("encryptedPassword", trainee.getUser().getPassword());
        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
        verify(userService, times(1)).changePassword(username, newPassword);
    }

    @Test
    void testChangeStatus_Success() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(trainee));
        when(userService.changeStatus(username)).thenReturn(true);

        traineeService.changeStatus(username );

        assertTrue(trainee.getUser().getIsActive());
        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
        verify(userService, times(1)).changeStatus(username);
    }

    @Test
    public void testDeleteTraineeByUserName()  {
        String username = "testUsername";
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);

        when(traineeDAO.findTraineeByUserUserName(username)).thenReturn(Optional.of(existingTrainee));

        assertDoesNotThrow(() -> traineeService.deleteTraineeByUserName(username));

        verify(traineeDAO, times(1)).findTraineeByUserUserName(username);
        verify(traineeDAO, times(1)).deleteTraineeByUserUserName(username);
    }

}