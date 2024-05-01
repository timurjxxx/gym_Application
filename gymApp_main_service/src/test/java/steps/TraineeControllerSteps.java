package steps;

import com.gypApp_main.controller.TraineeController;
import com.gypApp_main.exception.UserNotFoundException;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.model.User;
import com.gypApp_main.service.TraineeService;
import com.gypApp_main.service.TrainerService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TraineeControllerSteps {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TraineeController traineeController;
    private ResponseEntity<Void> response;
    private ResponseEntity<String> responseString;

    private String traineeUsername;
    private Trainee updatedTrainee ;
    private Trainee trainee;

    private Exception thrownException;

    private String invalidUsername;
    private ResponseEntity<String> responseEntity;
    private String username;

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


    @When("the delete trainee profile request is sent")
    public void theDeleteTraineeProfileRequestIsSent() {
        Mockito.doNothing().when(traineeService).deleteTraineeByUserName(traineeUsername);

        response = traineeController.deleteTraineeProfile(traineeUsername);
    }

    @Then("the trainee profile should be returned")
    public void theTraineeProfileShouldBeReturned() {
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
    ////////////////////////////////////////////// bad scenario

    @Given("a trainee with username {string}")
    public void givenTraineeWithUsername(String traineeUsername) {
        // Mock trainee with the given username
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("testname");
        user.setUserName("testUsername");
        trainee.setUser(user);
        when(traineeService.selectTraineeByUserName(traineeUsername)).thenReturn(trainee);
    }

    @Given("the following list of trainers:")
    public void givenListOfTrainers(List<String> trainerUsernames) {
        // Mock trainers with the given usernames
        Trainer trainer = new Trainer();
        Trainee trainee1 = new Trainee();
        for (String trainerUsername : trainerUsernames) {
            User user = new User();
            user.setFirstName("testname");
            user.setUserName("testUsername");
            trainee1.setUser(user);
            when(trainerService.selectTrainerByUserName(trainerUsername)).thenReturn(trainer);
        }
    }

    @When("the trainers list update request is sent to the API with username {string}")
    public void whenTrainersListUpdateRequestSentToAPI(String traineeUsername) {
        Map<String, Object> jsonData = Map.of("traineeUsername", traineeUsername, "trainerUsernames", List.of("trainerUsername1", "trainerUsername2"));

        responseString = traineeController.updateTraineeTrainersList(traineeUsername, jsonData);
    }


    @Then("the API should return a successful response trainee")
    public void theAPIShouldReturnASuccessfulResponseTrainee() {
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Given("invalid trainee username for deleting")
    public void invalidTraineeUsernameForDeleting() {
        invalidUsername = "invalid username";

    }

    @When("the delete ivalid trainee profile request is sent")
    public void theDeleteIvalidTraineeProfileRequestIsSent() {

        try {
            doThrow(UserNotFoundException.class).when(traineeService).deleteTraineeByUserName(invalidUsername);
            response = traineeController.deleteTraineeProfile(invalidUsername);
        } catch (UserNotFoundException e) {
            thrownException = e;
            response = ResponseEntity.notFound().build();
        }

    }

    @Then("the trainee profile should return bad response")
    public void theTraineeProfileShouldReturnBadResponse() {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Given("a trainee username and updated trainer details")
    public void aTraineeUsernameAndUpdatedTrainerDetails() {

        username = "test";
        trainee = new Trainee();
        User user = new User();
        user.setUserName(username);
        trainee.setUser(user);
    }

    @When("the update trainee profile request is sent")
    public void theUpdateTraineeProfileRequestIsSent() {
        when(traineeService.updateTrainee(Mockito.eq(username), any(Trainee.class))).thenReturn(trainee);
        responseEntity = traineeController.updateTraineeProfile(username, trainee);
    }

    @Then("the trainee profile should be updated")
    public void theTraineeProfileShouldBeUpdated() {
        Assertions.assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Given("a trainee invalid username and updated trainer details")
    public void aTraineeInvalidUsernameAndUpdatedTrainerDetails() {

        invalidUsername = "test";
        updatedTrainee = new Trainee();
        User user = new User();
        user.setUserName("username");
        user.setFirstName("testname");
        user.setUserName("testUsername");
        updatedTrainee.setTraineeTrainings(new ArrayList<>());
        updatedTrainee.setDateOfBirth(LocalDate.now());
        updatedTrainee.setAddress("adress");
        updatedTrainee.setUser(user);

    }

    @When("the update invalid trainee profile request is sent")
    public void theUpdateInvalidTraineeProfileRequestIsSent() {
        try {
            when(traineeService.updateTrainee(invalidUsername, updatedTrainee)).thenThrow(UserNotFoundException.class);
            responseEntity = traineeController.updateTraineeProfile(invalidUsername, updatedTrainee);
        } catch (UserNotFoundException e) {
            thrownException = e;
            responseString = ResponseEntity.notFound().build();
        }
    }

    @Then("the trainee invalid profile should not found")
    public void theTraineeInvalidProfileShouldNotFound() {
        Assertions.assertNotNull(responseString);
        Assertions.assertEquals(404, responseString.getStatusCodeValue());
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof UserNotFoundException);
    }
}
