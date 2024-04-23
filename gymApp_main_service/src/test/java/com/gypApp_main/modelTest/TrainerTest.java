package com.gypApp_main.modelTest;

import com.gypApp_main.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerTest {

    @Test
    public void testGetterAndSetterForSpecialization() {
        Trainer trainer = new Trainer();
        TrainingType specialization = new TrainingType();
        trainer.setSpecialization(specialization);
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    public void testGetterAndSetterForUser() {
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);
        assertEquals(user, trainer.getUser());
    }

    @Test
    public void testGetterAndSetterForTrainees() {
        Trainer trainer = new Trainer();
        Set<Trainee> trainees = new HashSet<>();
        trainer.setTrainees(trainees);
        assertEquals(trainees, trainer.getTrainees());
    }

    @Test
    public void testGetterAndSetterForTraineeTrainings() {
        Trainer trainer = new Trainer();
        List<Training> traineeTrainings = List.of(new Training(), new Training());
        trainer.setTraineeTrainings(traineeTrainings);
        assertEquals(traineeTrainings, trainer.getTraineeTrainings());
    }

    @Test
    public void testEqualsAndHashCode() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);

        Trainer trainer2 = new Trainer();
        trainer2.setId(1L);

        assertEquals(trainer1, trainer2);
        assertEquals(trainer1.hashCode(), trainer2.hashCode());
    }

    @Test
    public void testToString() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        trainer.setUser(user);

        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Weightlifting");
        trainer.setSpecialization(specialization);

        Set<Trainee> trainees = new HashSet<>();
        trainees.add(new Trainee());
        trainees.add(new Trainee());
        trainer.setTrainees(trainees);

        String expectedToString = "Trainer{First name=' John'Last name=' Doe'specialization='Weightlifting',Trainees list =" + trainees + '}';
        assertEquals(expectedToString, trainer.toString());
    }
}