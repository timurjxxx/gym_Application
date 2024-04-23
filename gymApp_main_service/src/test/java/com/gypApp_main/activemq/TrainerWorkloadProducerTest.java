package com.gypApp_main.activemq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_main.dto.TrainerWorkloadRequest;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.Training;
import com.gypApp_main.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerWorkloadProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private ObjectMapper objectMapper;
    @Value("${activemq.queue}")
    private String workload;  //

    @InjectMocks
    private TrainerWorkloadProducer producer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateWorkLoad_Success() throws JsonProcessingException {
        Training training = new Training();
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("test");
        user.setLastName("test");
        user.setUserName("username");
        user.setIsActive(true);
        trainer.setUser(user);
        String action = "ADD";
        training.setTrainer(trainer);

        String jsonRequest = "JSON representation of request";

        when(objectMapper.writeValueAsString(any(TrainerWorkloadRequest.class))).thenReturn(jsonRequest);

        producer.updateWorkLoad(training, action);

        verify(jmsTemplate).convertAndSend(eq(workload), eq(jsonRequest));
    }
}
