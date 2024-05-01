package steps;

import com.gypApp_workLoadService.controller.TrainerWorkloadController;
import com.gypApp_workLoadService.dto.ActionType;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TrainerWorkloadControllerSteps {


    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @InjectMocks
    private TrainerWorkloadController trainerWorkloadController;

    private ResponseEntity<Void> responseEntity;
    private TrainerWorkloadRequest request;

    private Exception exception;

    public TrainerWorkloadControllerSteps() {
        MockitoAnnotations.openMocks(this);

    }

    @Given("^a trainer workload request is received$")
    public void aTrainerWorkloadRequestIsReceived() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();
        doNothing().when(trainerWorkloadService).updateWorkload(request);
    }

    @When("^the trainer workload is updated$")
    public void theTrainerWorkloadIsUpdated() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();
        responseEntity = trainerWorkloadController.updateWorkload(request);
    }

    @Then("^the trainer workload should be successfully updated$")
    public void theTrainerWorkloadShouldBeSuccessfullyUpdated() {
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }

    ///////////////////////////////////////////////////////////////////////////
    @Given("^an invalid trainer workload request is received$")
    public void anInvalidTrainerWorkloadRequestIsReceived() {
        request = TrainerWorkloadRequest.builder()
                .trainerUsername(null)
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .type(ActionType.ADD)
                .build();
    }

    @Then("the update should fail with a bad request response")
    public void theUpdateShouldFailWithABadRequestResponse() {
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;

    }

    @Given("an nullpoint username trainer workload request is received")
    public void anNullpointUsernameTrainerWorkloadRequestIsReceived() {
        request = null;
    }

    @When("the null point trainer send endpoint")
    public void theNullPointTrainerSendEndpoint() {
        try {
            doThrow(NullPointerException.class).when(trainerWorkloadService).updateWorkload(request);
            responseEntity = trainerWorkloadController.updateWorkload(request);
        } catch (NullPointerException e) {
            exception = e;
            responseEntity = ResponseEntity.badRequest().build();
        }
    }

    @Then("the update should fail")
    public void theUpdateShouldFail() {
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NullPointerException);

    }

}
