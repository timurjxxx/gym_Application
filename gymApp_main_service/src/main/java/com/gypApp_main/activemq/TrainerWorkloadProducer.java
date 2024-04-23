package com.gypApp_main.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_main.dto.TrainerWorkloadRequest;
import com.gypApp_main.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerWorkloadProducer {

    private final JmsTemplate jmsTemplate;
    private final String workload;

    private final ObjectMapper objectMapper;


    @Autowired
    public TrainerWorkloadProducer(JmsTemplate jmsTemplate, @Value("${activemq.queue}") String queueName, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.workload = queueName;
        this.objectMapper = objectMapper;
    }

    public void updateWorkLoad(Training training, String action) throws JsonProcessingException {
        log.debug("Training details {}", training);
        log.info("Action type {}", action);

        TrainerWorkloadRequest request = createRequest(training, action);
        String jsonRequest = objectMapper.writeValueAsString(request);
        jmsTemplate.convertAndSend(workload, jsonRequest);
        log.info("Message sent to ActiveMQ queue: {}", workload);
    }

    public TrainerWorkloadRequest createRequest(Training training, String action) {

        String username = training.getTrainer().getUser().getUserName();
        String firstName = training.getTrainer().getUser().getFirstName();
        String lastName = training.getTrainer().getUser().getLastName();
        Boolean isAcitve = training.getTrainer().getUser().getIsActive();
        log.info("Create request with trainer details {}", training);
        log.info("Action type {}", action);

        return TrainerWorkloadRequest.builder()
                .trainerUsername(username)
                .trainerFirstname(firstName)
                .trainerLastname(lastName)
                .isActive(isAcitve)
                .type(action)
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();

    }

}
