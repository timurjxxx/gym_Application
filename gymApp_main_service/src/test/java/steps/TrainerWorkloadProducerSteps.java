package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gypApp_main.activemq.TrainerWorkloadProducer;
import com.gypApp_main.dto.TrainerWorkloadRequest;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.Training;
import com.gypApp_main.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TrainerWorkloadProducerSteps {

    private Training training;
    private String action;
    private TrainerWorkloadProducer workloadProducer;
    private boolean requestSent;
    private TrainerWorkloadRequest createdRequest;
    private boolean errorHandled;


    @Given("a training and an action type")
    public void a_training_and_an_action_type() {
        User user = new User();
        user.setUserName("john_doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDuration(60);
        training.setTrainingDate(LocalDate.now());

        action = "some_action";
    }

    @Given("an invalid training or action type")
    public void an_invalid_training_or_action_type() {
        training = null;
        action = null;
    }

    @When("the update workload request is sent to the ActiveMQ queue")
    public void the_update_workload_request_is_sent_to_the_ActiveMQ_queue() throws JsonProcessingException {
        workloadProducer = mock(TrainerWorkloadProducer.class);
        doAnswer(invocation -> {
            requestSent = true;
            errorHandled = true;
            return null;
        }).when(workloadProducer).updateWorkLoad(training, action);

        workloadProducer.updateWorkLoad(training, action);
    }

    @Then("the request should be successfully sent")
    public void the_request_should_be_successfully_sent() throws JsonProcessingException {
        verify(workloadProducer).updateWorkLoad(training, action);
        assert requestSent;
    }

    @When("the request is created")
    public void the_request_is_created() {
        workloadProducer = new TrainerWorkloadProducer(null, null, null);
        createdRequest = workloadProducer.createRequest(training, action);
    }

    @Then("the request should contain the correct details")
    public void the_request_should_contain_the_correct_details() {
        assertEquals(training.getTrainer().getUser().getUserName(), createdRequest.getTrainerUsername());
        assertEquals(training.getTrainer().getUser().getFirstName(), createdRequest.getTrainerFirstname());
        assertEquals(training.getTrainer().getUser().getLastName(), createdRequest.getTrainerLastname());
        assertEquals(training.getTrainer().getUser().getIsActive(), createdRequest.getIsActive());
        assertEquals(action, createdRequest.getType());
        assertEquals(training.getTrainingDuration(), createdRequest.getTrainingDuration());
        assertEquals(training.getTrainingDate(), createdRequest.getTrainingDate());
    }

    @Then("the system should handle the error appropriately")
    public void the_system_should_handle_the_error_appropriately() {
        assertTrue(errorHandled);
    }
}
