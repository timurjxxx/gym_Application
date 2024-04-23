package steps;

import com.gypApp_workLoadService.controller.TrainerWorkloadController;
import com.gypApp_workLoadService.dto.ActionType;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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


    public TrainerWorkloadControllerSteps(){
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
        // Create an invalid request (for example, missing required fields)
        request = TrainerWorkloadRequest.builder()
                .trainerUsername(null) // Invalid: Username is null
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .type(ActionType.ADD)
                .build();
    }

    @Then("the update should fail with a bad request response")
    public void theUpdateShouldFailWithABadRequestResponse() {
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;

    }
}
