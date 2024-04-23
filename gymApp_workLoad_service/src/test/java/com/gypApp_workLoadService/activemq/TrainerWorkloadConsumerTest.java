package com.gypApp_workLoadService.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.JmsException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TrainerWorkloadConsumerTest {
    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private TrainerWorkloadConsumer consumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValidMessage_ValidRequest() {
        TrainerWorkloadRequest validRequest = new TrainerWorkloadRequest();
        validRequest.setTrainingDuration(60);
        validRequest.setTrainingDate(LocalDate.now());
        TrainerWorkloadService mockService = Mockito.mock(TrainerWorkloadService.class);
        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        JmsTemplate jmsTemplate1 = Mockito.mock(JmsTemplate.class);
        TrainerWorkloadConsumer consumer = new TrainerWorkloadConsumer(mockService, mockMapper,jmsTemplate1);
        assertTrue(consumer.isValidMessage(validRequest));
    }

    @Test
    void testIsValidMessage_InvalidDuration() {
        TrainerWorkloadRequest invalidRequest = new TrainerWorkloadRequest();
        invalidRequest.setTrainingDuration(-10);
        invalidRequest.setTrainingDate(LocalDate.now());

        TrainerWorkloadService mockService = Mockito.mock(TrainerWorkloadService.class);
        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        JmsTemplate jmsTemplate1 = Mockito.mock(JmsTemplate.class);
        TrainerWorkloadConsumer consumer = new TrainerWorkloadConsumer(mockService, mockMapper, jmsTemplate1);

        assertFalse(consumer.isValidMessage(invalidRequest));
    }

    @Test
    void testIsValidMessage_InvalidYear() {
        TrainerWorkloadRequest invalidRequest = new TrainerWorkloadRequest();
        invalidRequest.setTrainingDuration(60);
        invalidRequest.setTrainingDate(LocalDate.of(2000, 1, 1));

        TrainerWorkloadService mockService = Mockito.mock(TrainerWorkloadService.class);
        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        JmsTemplate jmsTemplate1 = Mockito.mock(JmsTemplate.class);
        TrainerWorkloadConsumer consumer = new TrainerWorkloadConsumer(mockService, mockMapper,jmsTemplate1);

        assertFalse(consumer.isValidMessage(invalidRequest));
    }


    @Test
    void testReceiveMessage_ValidMessage() throws JsonProcessingException, JsonProcessingException {
        TrainerWorkloadService mockService = Mockito.mock(TrainerWorkloadService.class);
        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        JmsTemplate mockJmsTemplate = Mockito.mock(JmsTemplate.class);

        String jsonRequest = "{\"trainingDuration\": 60, \"trainingDate\": \"2024-04-10\"}";

        TrainerWorkloadRequest validRequest = new TrainerWorkloadRequest();
        validRequest.setTrainingDuration(60);
        validRequest.setTrainingDate(LocalDate.of(2024, 4, 10));

        when(mockMapper.readValue(jsonRequest, TrainerWorkloadRequest.class)).thenReturn(validRequest);

        TrainerWorkloadConsumer consumer = new TrainerWorkloadConsumer(mockService, mockMapper, mockJmsTemplate);

        consumer.receiveMessage(jsonRequest);

        verify(mockService, times(1)).updateWorkload(validRequest);
    }
}
