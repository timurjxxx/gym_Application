package steps;


import com.gypApp_main.controller.AuthenticationController;
import com.gypApp_main.dto.ChangeLoginRequest;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.model.User;
import com.gypApp_main.service.AuthService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class AuthenticationControllerSteps {

    @InjectMocks
    private AuthenticationController authenticationController;

    private ResponseEntity<?> response;
    @Mock
    private AuthService service;

    private LoginRequest loginRequest = new LoginRequest();
    private LoginRequest invalidLoginRequest = new LoginRequest();
    private Trainer trainer = new Trainer();
    private Trainee trainee = new Trainee();
    private ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest();

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

    //////////////////
    @Given("an invalid request")
    public void anInvalidRequest() {
        invalidLoginRequest.setUsername("testUsername");
        invalidLoginRequest.setPassword("testPassword");
    }

    @When("the invalid request is sent to the API")
    public void theInvalidRequestIsSentToTheAPI() {
        response = authenticationController.login(invalidLoginRequest);

    }

    @Then("the API should return a bad request response")
    public void theAPIShouldReturnABadRequestResponse() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


    }
}
