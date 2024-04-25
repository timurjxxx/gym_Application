package steps;

import com.gypApp_main.controller.TraineeController;
import com.gypApp_main.exception.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private Exception thrownException;

    private String invalidUsername;

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
        user.setFirstName("testname");
        user.setUserName("testUsername");
        trainee.setUser(user);
    }

    @Given("a trainee username and updated trainee details")
    public void aTraineeUsernameAndUpdatedTraineeDetails() {
        traineeUsername = "testTrainee";
        updatedTrainee = new Trainee();
        User user = new User();
        user.setUserName("username");
        updatedTrainee.setAddress("Updated Address");
        updatedTrainee.setUser(user);
        updatedTrainee.setTrainers(Collections.emptySet());
        when(traineeService.updateTrainee(traineeUsername, updatedTrainee)).thenReturn(updatedTrainee);

    }

    @When("the get trainee profile request is sent")
    public void theGetTraineeProfileRequestIsSent() {
        Trainee dummyTrainee = new Trainee();
        when(traineeService.selectTraineeByUserName(traineeUsername)).thenReturn(dummyTrainee);

        ResponseEntity<String> response = traineeController.getTraineeProfile(traineeUsername);

        assertEquals(200, response.getStatusCodeValue());
    }

    @When("the update trainee profile request is sent")
    public void theUpdateTraineeProfileRequestIsSent() {
        assertNotNull(traineeUsername);
        assertNotNull(updatedTrainee);

        when(traineeService.updateTrainee(traineeUsername, updatedTrainee)).thenReturn(updatedTrainee);

        responseString = traineeController.updateTraineeProfile(traineeUsername, updatedTrainee);

    }

    @When("the delete trainee profile request is sent")
    public void theDeleteTraineeProfileRequestIsSent() {
        Mockito.doNothing().when(traineeService).deleteTraineeByUserName(traineeUsername);

        response = traineeController.deleteTraineeProfile(traineeUsername);
    }

    @Then("the trainee profile should be returned")
    public void theTraineeProfileShouldBeReturned() {
    }

    @Then("the trainee profile should be updated")
    public void theTraineeProfileShouldBeUpdated() {
        Trainee updatedTrainee = new Trainee();
        updatedTrainee = traineeService.updateTrainee(traineeUsername, updatedTrainee);
        assertNotNull(updatedTrainee);
        assertEquals("Updated Address", updatedTrainee.getAddress());
    }

    @Then("the trainee profile should be deleted and return 200")
    public void theTraineeProfileShouldBeDeletedAndReturn200() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Given("an invalid trainee username")
    public void anInvalidTraineeUsername() {
        invalidUsername = "non_existing_username";

    }

    @When("the get trainee profile request with invalid username is sent")
    public void theGetTraineeProfileRequestWithInvalidUsernameIsSent() {
        try {
            when(traineeService.selectTraineeByUserName(invalidUsername)).thenThrow(UserNotFoundException.class);
            responseString = traineeController.getTraineeProfile(invalidUsername);
        } catch (UserNotFoundException e) {
            thrownException = e;
            responseString = ResponseEntity.notFound().build();
        }
    }


    @Then("the API should return a not found response for trainee")
    public void theAPIShouldReturnANotFoundResponseForTrainee() {
        Assertions.assertNotNull(responseString);
        Assertions.assertEquals(404, responseString.getStatusCodeValue());
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof UserNotFoundException);
    }

}
