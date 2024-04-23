package com.gypApp_workLoadService.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerTest {

    @Test
    public void testToString() {
        Trainer trainer = Trainer.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingSummaryDuration(Collections.singletonMap("Running", 60))
                .build();

        String expected = "John Doe {Running=60}";
        assertEquals(expected, trainer.toString());
    }
    @Test
    public void testGettersAndSetters() {
        Trainer trainer = new Trainer();

        trainer.setUsername("john_doe");
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setIsActive(true);
        trainer.setTrainingSummaryDuration(Collections.singletonMap("Running", 60));

        assertEquals("john_doe", trainer.getUsername());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertTrue(trainer.getIsActive());
        assertEquals(Collections.singletonMap("Running", 60), trainer.getTrainingSummaryDuration());
    }
}
