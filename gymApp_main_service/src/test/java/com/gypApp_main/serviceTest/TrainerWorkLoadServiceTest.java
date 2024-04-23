package com.gypApp_main.serviceTest;

import com.gypApp_main.dto.TrainerWorkloadRequest;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.Training;
import com.gypApp_main.model.User;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.TrainerWorkLoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerWorkLoadServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private JWTProvider provider;

    @InjectMocks
    private TrainerWorkLoadService workLoadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testCreateRequest() {
        Training training = new Training();
        training.setTrainingDuration(60);
        training.setTrainingDate(LocalDate.now());
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setUserName("username");
        user.setLastName("Doe");
        user.setIsActive(true);
        trainer.setUser(user);
        training.setTrainer(trainer);
        String action = "ADD";
        TrainerWorkloadRequest request = workLoadService.createRequest(training, action);
        assertEquals("username", request.getTrainerUsername());
        assertEquals("John", request.getTrainerFirstname());
        assertEquals("Doe", request.getTrainerLastname());
        assertEquals(true, request.getIsActive());
        assertEquals("ADD", request.getType());
        assertEquals(60, request.getTrainingDuration());
        assertEquals(LocalDate.now(), request.getTrainingDate());


    }


}
