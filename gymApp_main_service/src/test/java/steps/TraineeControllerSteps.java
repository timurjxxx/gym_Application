package steps;

import com.gypApp_main.controller.TraineeController;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.User;
import com.gypApp_main.service.TraineeService;
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

import java.util.Collections;

import static org.mockito.Mockito.when;

public class TraineeControllerSteps {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;
    private ResponseEntity<Void> response;
    private ResponseEntity<String> responseString;

    private String traineeUsername;
    private Trainee updatedTrainee;
    private Trainee trainee;

    public TraineeControllerSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a trainee username")
    public void aTraineeUsername() {
        traineeUsername = "testTrainee";
    }

    @Given("a trainee username for deleting")
    public void aTraineeUsernameForDeleting() {
        trainee = new Trainee();
        User user = new User();
        user.setUserName("testUsername");
        trainee.setUser(user);
    }

    @Given("a trainee username and updated trainee details")
    public void aTraineeUsernameAndUpdatedTraineeDetails() {
        traineeUsername = "testTrainee";
        updatedTrainee = new Trainee();
        // Set updated details for the trainee
        updatedTrainee.setAddress("Updated Address");
        updatedTrainee.setTrainers(Collections.emptySet());
        // Mock the behavior of traineeService to update the trainee profile
        Mockito.when(traineeService.updateTrainee(traineeUsername, updatedTrainee));
    }

    @When("the get trainee profile request is sent")
    public void theGetTraineeProfileRequestIsSent() {
        // Mock the behavior of traineeService to return a dummy trainee
        Trainee dummyTrainee = new Trainee();
        when(traineeService.selectTraineeByUserName(traineeUsername)).thenReturn(dummyTrainee);

        ResponseEntity<String> response = traineeController.getTraineeProfile(traineeUsername);

        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @When("the update trainee profile request is sent")
    public void theUpdateTraineeProfileRequestIsSent() {
        responseString = traineeController.updateTraineeProfile(traineeUsername, updatedTrainee);

    }

    @When("the delete trainee profile request is sent")
    public void theDeleteTraineeProfileRequestIsSent() {
        Mockito.doNothing().when(traineeService).deleteTraineeByUserName(traineeUsername);

        // Invoke the endpoint to delete the trainee profile
        response = traineeController.deleteTraineeProfile(traineeUsername);
    }

    @Then("the trainee profile should be returned")
    public void theTraineeProfileShouldBeReturned() {
    }

    @Then("the trainee profile should be updated")
    public void theTraineeProfileShouldBeUpdated() {
    }

    @Then("the trainee profile should be deleted and return 200")
    public void theTraineeProfileShouldBeDeletedAndReturn200() {
        // Assert the response
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
