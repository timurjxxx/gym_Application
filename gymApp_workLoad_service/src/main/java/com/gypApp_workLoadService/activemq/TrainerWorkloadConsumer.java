package com.gypApp_workLoadService.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadConsumer {
    private final TrainerWorkloadService trainerWorkloadService;
    private final ObjectMapper objectMapper;
    private final JmsTemplate jmsTemplate;

    @Value("${activemq.dlq}")
    private String dlqName;

    @JmsListener(destination = "${activemq.queue}")
    public void receiveMessage(String jsonRequest) throws JsonProcessingException {
        try {
            log.info("Received trainer workload message: {}", jsonRequest);
            TrainerWorkloadRequest request = objectMapper.readValue(jsonRequest, TrainerWorkloadRequest.class);
            if (isValidMessage(request)) {
                trainerWorkloadService.updateWorkload(request);
            } else {
                sendToDLQ(jsonRequest);
            }
        } catch (JsonProcessingException e) {
            sendToDLQ(jsonRequest);
            log.error("Error processing JSON message: {}", e.getMessage());
        }
    }
    boolean isValidMessage(TrainerWorkloadRequest request) {
        return request.getTrainingDuration() > 0 && (request.getTrainingDate().getYear() >= LocalDate.now().getYear());
    }
    void sendToDLQ(String jsonRequest) {
        try {
            jmsTemplate.convertAndSend(dlqName, jsonRequest);
            log.info("Invalid message sent to dead-letter queue: {}", dlqName);
        } catch (Exception e) {
            log.error("Error sending message to dead-letter queue: {}", e.getMessage());
        }
    }


}
