package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gypApp_main.controller.TrainingController;
import com.gypApp_main.model.*;
import com.gypApp_main.service.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TrainingControllerSteps {

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private TrainingService trainingService;

    private ResponseEntity<Void> createTrainingResponse;
    private ResponseEntity<?> deleteTrainingResponse;
    private ResponseEntity<String> traineeTrainingsResponse;
    private ResponseEntity<String> trainerTrainingsResponse;

    private Training validTraining;
    private String existingTrainingName;
    private String traineeUsername;
    private String trainerUsername;
    private TrainingSearchCriteria traineeCriteria;
    private TrainingSearchCriteria trainerCriteria;

    public TrainingControllerSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a valid training")
    public void givenValidTraining() {
        // Initialize a valid training object
        validTraining = new Training();
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();
        TrainingType trainingType = new TrainingType();
        User user = new User();
        user.setUserName("testname");
        trainer.setUser(user);
        trainee.setUser(user);
        validTraining.setTrainer(trainer);
        validTraining.setTrainee(trainee);
        validTraining.setTrainingTypes(trainingType);

    }

    @Given("an existing training name")
    public void givenExistingTrainingName() {
        existingTrainingName = "existingTrainingName";
    }

    @Given("a trainee username {string}")
    public void givenTraineeUsername(String username) {
        traineeUsername = username;
    }

    @Given("a trainer username {string}")
    public void givenTrainerUsername(String username) {
        trainerUsername = username;
    }

    @Given("a criteria for trainee trainings")
    public void givenCriteriaForTraineeTrainings() {
        traineeCriteria = new TrainingSearchCriteria();
    }

    @Given("a criteria for trainer trainings")
    public void givenCriteriaForTrainerTrainings() {
        trainerCriteria = new TrainingSearchCriteria();
    }

    @When("the create training request is sent")
    public void whenCreateTrainingRequestSent() throws JsonProcessingException {
        when(trainingService.addTraining(any(Training.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(validTraining);
        createTrainingResponse = trainingController.createTraining(validTraining);
    }

    @When("the delete training request is sent")
    public void whenDeleteTrainingRequestSent() throws JsonProcessingException {
       doNothing().when(trainingService).deleteTraining(existingTrainingName);
        deleteTrainingResponse = trainingController.deleteTraining(existingTrainingName);
    }

    @When("the get trainee trainings by criteria request is sent")
    public void whenGetTraineeTrainingsByCriteriaRequestSent() {
        when(trainingService.getTraineeTrainingsByCriteria(traineeUsername, traineeCriteria))
                .thenReturn(Collections.singletonList(validTraining));
        traineeTrainingsResponse = trainingController.getTraineeTrainingsByCriteria(traineeUsername, traineeCriteria);
    }

    @When("the get trainer trainings by criteria request is sent")
    public void whenGetTrainerTrainingsByCriteriaRequestSent() {
        when(trainingService.getTrainerTrainingsByCriteria(trainerUsername, trainerCriteria))
                .thenReturn(Collections.singletonList(validTraining));
        trainerTrainingsResponse = trainingController.getTrainerTrainingsByCriteria(trainerUsername, trainerCriteria);
    }

    @Then("the training should be successfully created")
    public void thenTrainingSuccessfullyCreated() {
        Assertions.assertEquals(200, createTrainingResponse.getStatusCodeValue());
    }

    @Then("the training should be successfully deleted")
    public void thenTrainingSuccessfullyDeleted() {
        Assertions.assertEquals(200, deleteTrainingResponse.getStatusCodeValue());
    }

    @Then("the trainee trainings should be returned")
    public void thenTraineeTrainingsReturned() {
        Assertions.assertNotNull(traineeTrainingsResponse);
    }

    @Then("the trainer trainings should be returned")
    public void thenTrainerTrainingsReturned() {
        Assertions.assertNotNull(trainerTrainingsResponse);
    }
}
