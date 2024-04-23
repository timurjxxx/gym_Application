package com.gypApp_main.modelTest;

import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.model.Training;
import com.gypApp_main.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TrainingTest {

    @Test
    public void testGetterAndSetterForId() {
        Training training = new Training();
        Long id = 1L;
        training.setId(id);
        assertEquals(id, training.getId());
    }

    @Test
    public void testGetterAndSetterForTrainingName() {
        Training training = new Training();
        String trainingName = "Test Training";
        training.setTrainingName(trainingName);
        assertEquals(trainingName, training.getTrainingName());
    }

    @Test
    public void testGetterAndSetterForTrainingDate() {
        Training training = new Training();
        LocalDate trainingDate = LocalDate.now();
        training.setTrainingDate(trainingDate);
        assertEquals(trainingDate, training.getTrainingDate());
    }

    @Test
    public void testGetterAndSetterForTrainingDuration() {
        Training training = new Training();
        Integer trainingDuration = 60;
        training.setTrainingDuration(trainingDuration);
        assertEquals(trainingDuration, training.getTrainingDuration());
    }

    @Test
    public void testGetterAndSetterForTrainingTypes() {
        Training training = new Training();
        TrainingType trainingType = new TrainingType();
        training.setTrainingTypes(trainingType);
        assertEquals(trainingType, training.getTrainingTypes());
    }

    @Test
    public void testGetterAndSetterForTrainee() {
        Training training = new Training();
        Trainee trainee = new Trainee();
        training.setTrainee(trainee);
        assertEquals(trainee, training.getTrainee());
    }

    @Test
    public void testGetterAndSetterForTrainer() {
        Training training = new Training();
        Trainer trainer = new Trainer();
        training.setTrainer(trainer);
        assertEquals(trainer, training.getTrainer());
    }

    @Test
    public void testEqualsAndHashCode() {
        Training training1 = new Training();
        training1.setId(1L);
        Training training2 = new Training();
        training2.setId(1L);
        Training training3 = new Training();
        training3.setId(2L);

        assertEquals(training1, training2);
        assertNotEquals(training1, training3);
        assertEquals(training1.hashCode(), training2.hashCode());
        assertNotEquals(training1.hashCode(), training3.hashCode());
    }


}