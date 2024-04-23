package com.gypApp_main.modelTest;

import com.gypApp_main.model.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeTest {

    @Test
    public void testEqualsAndHashCode() {
        Trainee trainee1 = new Trainee();
        trainee1.setId(1L);

        Trainee trainee2 = new Trainee();
        trainee2.setId(1L);

        Trainee trainee3 = new Trainee();
        trainee3.setId(2L);

        // Test equals
        assertTrue(trainee1.equals(trainee2));
        assertFalse(trainee1.equals(trainee3));

        // Test hashCode
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
        assertNotEquals(trainee1.hashCode(), trainee3.hashCode());
    }

    @Test
    public void testToString() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("Test Address");

        // Customize the expected output based on your implementation
        String expectedToString = "Trainee{null, dateOfBirth=1990-01-01, address='Test Address'}";

        assertEquals(expectedToString, trainee.toString());
    }
}