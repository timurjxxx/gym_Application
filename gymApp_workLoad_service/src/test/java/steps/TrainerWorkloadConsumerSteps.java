package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_workLoadService.activemq.TrainerWorkloadConsumer;
import com.gypApp_workLoadService.dto.ActionType;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainerWorkloadConsumerSteps {


    @InjectMocks
    private TrainerWorkloadConsumer trainerWorkloadConsumer;
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @Mock
    private ObjectMapper objectMapper;


    private String jsonRequest;

    private String  dlqName;
    private TrainerWorkloadRequest request ;

    private String invalidJsonRequest;
    private RuntimeException thrownException;

    public TrainerWorkloadConsumerSteps() {
        MockitoAnnotations.openMocks(this);
    }
    @Given("a valid JSON request")
    public void aValidJsonRequest() throws JsonProcessingException {
        jsonRequest = "{\"trainerUsername\":\"john123\",\"trainerFirstname\":\"John\",\"trainerLastname\":\"Doe\",\"trainingDate\":\"2024-04-23\",\"trainingDuration\":60,\"isActive\":true}";
        request = TrainerWorkloadRequest.builder()
                .trainerFirstname("John")
                .trainingDuration(60)
                .trainerUsername("john123")
                .trainerLastname("Doe")
                .trainingDate( LocalDate.parse("2024-04-23"))
                .isActive(true)
                .build();
        when(objectMapper.readValue(jsonRequest, TrainerWorkloadRequest.class)).thenReturn(request);
    }


    @When("the message is received")
    public void theMessageIsReceived() throws JsonProcessingException {
        trainerWorkloadConsumer.receiveMessage(jsonRequest);
    }


    @Then("the workload should be updated")
    public void theWorkloadShouldBeUpdated() {
        verify(trainerWorkloadService).updateWorkload(request);
    }


    @Given("an invalid JSON request")
    public void anInvalidJsonRequest() throws JsonProcessingException {
        invalidJsonRequest = "{\"trainerUsername\":\"john123\",\"trainerFirstname\":\"John\",\"trainerLastname\":\"Doe\",\"trainingDate\":\"2026-04-23\",\"trainingDuration\":-60,\"isActive\":true}";
        request = TrainerWorkloadRequest.builder()
                .trainerFirstname("John")
                .trainingDuration(-60)
                .trainerUsername("john123")
                .trainerLastname("Doe")
                .trainingDate( LocalDate.parse("2024-04-23"))
                .isActive(true)
                .build();
        when(objectMapper.readValue(invalidJsonRequest, TrainerWorkloadRequest.class)).thenReturn(request);
    }
    @When("the invalid message is received")
    public void theInvalidMessageIsReceived() throws JsonProcessingException {

        trainerWorkloadConsumer.receiveMessage(invalidJsonRequest);
    }


    @Then("the message should be sent to the dead-letter queue")
    public void theMessageShouldBeSentToTheDeadLetterQueue() {
        verify(jmsTemplate).convertAndSend(dlqName, invalidJsonRequest);

    }


}
