package com.gypApp_main.controllerTest;

import com.gypApp_main.controller.AuthenticationController;
import com.gypApp_main.dto.JwtResponse;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.User;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    @Mock
    private AuthService service;

    @Mock
    private JWTProvider provider;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest("username", "password");
        String token = "mocked_token";
        JwtResponse response1 = new JwtResponse(token);
        when(service.login(request)).thenReturn(response1);

        ResponseEntity<?> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, ((JwtResponse) Objects.requireNonNull(response.getBody())).getToken());
        verify(service, times(1)).login(request);
    }


    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setUserName("username");
        user.setIsActive(true);
        trainee.setUser(user);
        when(service.createTrainee(trainee)).thenReturn("Trainee created successfully");

        ResponseEntity<String> response = controller.createTrainee(trainee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trainee created successfully", response.getBody());
        verify(service, times(1)).createTrainee(trainee);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setUserName("username");
        user.setIsActive(true);
        trainer.setUser(user);
        when(service.createTrainer(trainer)).thenReturn("Trainer created successfully");

        ResponseEntity<String> response = controller.createTrainer(trainer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trainer created successfully", response.getBody());
        verify(service, times(1)).createTrainer(trainer);
    }




}
