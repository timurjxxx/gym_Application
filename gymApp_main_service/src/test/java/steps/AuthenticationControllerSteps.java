package steps;


import com.gypApp_main.controller.AuthenticationController;
import com.gypApp_main.dto.ChangeLoginRequest;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.exception.UserNotFoundException;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.model.User;
import com.gypApp_main.service.AuthService;
import com.gypApp_main.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AuthenticationControllerSteps {

    @InjectMocks
    private AuthenticationController authenticationController;

    private ResponseEntity<?> response;
    @Mock
    private AuthService service;
    @Mock
    private UserService userService;

    private LoginRequest loginRequest = new LoginRequest();
    private LoginRequest invalidLoginRequest = new LoginRequest();
    private Trainer trainer = new Trainer();
    private Trainee trainee = new Trainee();
    private ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest();
    private Exception exception;

    public AuthenticationControllerSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a valid login request")
    public void givenValidLoginRequest() {
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("testPassword");

    }

    @Given("a valid trainee creation request")
    public void givenValidTraineeCreationRequest() {
        User user = new User();
        user.setFirstName("name");
        user.setIsActive(true);
        user.setLastName("lastnaem");
        trainee.setAddress("test");
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setUser(user);
    }

    @Given("a valid trainer creation request")
    public void givenValidTrainerCreationRequest() {
        User user = new User();
        user.setFirstName("name");
        user.setIsActive(true);
        user.setLastName("lastnaem");
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Run");
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);
    }

    @Given("a valid change password request")
    public void givenValidChangePasswordRequest() {
        changeLoginRequest.setUsername("testUsername");
        changeLoginRequest.setOldPassword("oldPassword");
        changeLoginRequest.setNewPassword("newPassword");
    }

    @When("the login request is sent to the API")
    public void whenLoginRequestSentToAPI() {
        response = authenticationController.login(loginRequest);
    }

    @When("the trainee creation request is sent to the API")
    public void whenTraineeCreationRequestSentToAPI() {
        response = authenticationController.createTrainee(trainee);
    }

    @When("the trainer creation request is sent to the API")
    public void whenTrainerCreationRequestSentToAPI() {
        response = authenticationController.createTrainer(trainer);
    }

    @When("the change password request is sent to the API")
    public void whenChangePasswordRequestSentToAPI() {
        response = authenticationController.changeLogin(changeLoginRequest);
    }

    @Then("the API should return a successful response")
    public void thenApiShouldReturnSuccessfulResponse() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Given("a invalid change password request")
    public void aInvalidChangePasswordRequest() {
        changeLoginRequest.setUsername("invalid username");
        changeLoginRequest.setOldPassword("oldPassword");
        changeLoginRequest.setNewPassword("newPassword");
    }

    @When("the invalid change password request is sent to the API")
    public void theInvalidChangePasswordRequestIsSentToTheAPI() {
        when(service.changePassword(changeLoginRequest)).thenReturn(HttpStatus.BAD_REQUEST);
        response = authenticationController.changeLogin(changeLoginRequest);
    }

    @Then("the API should return a bad response")
    public void theAPIShouldReturnABadResponse() {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }


    @Given("a invalid trainer creation request")
    public void aInvalidTrainerCreationRequest() {
        User user = new User();
        user.setFirstName("name");
        user.setIsActive(true);
        trainer.setUser(user);
    }

    @When("the invalid trainer creation request is sent to the API")
    public void theInvalidTrainerCreationRequestIsSentToTheAPI() {

        try {

            when(service.createTrainer(trainer)).thenThrow(NullPointerException.class);
            response = authenticationController.createTrainer(trainer);
        } catch (NullPointerException e) {
            exception = e;
            response = ResponseEntity.badRequest().build();
        }
    }

    @Then("the API should return bad response")
    public void theAPIShouldReturnBadResponse() {

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NullPointerException);
    }

    @Given("a invalid trainee creation request")
    public void aInvalidTraineeCreationRequest() {
        User user = new User();
        user.setFirstName("name");
        user.setIsActive(true);
        trainer.setUser(user);
    }

    @When("the invalid trainee creation request is sent to the API")
    public void theInvalidTraineeCreationRequestIsSentToTheAPI() {

        try {
            when(service.createTrainee(trainee)).thenThrow(NullPointerException.class);
            response = authenticationController.createTrainee(trainee);
        } catch (NullPointerException e) {
            exception = e;
            response = ResponseEntity.badRequest().build();
        }
    }

    @Then("API should return bad response")
    public void apiShouldReturnBadResponse() {

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NullPointerException);
    }
}
