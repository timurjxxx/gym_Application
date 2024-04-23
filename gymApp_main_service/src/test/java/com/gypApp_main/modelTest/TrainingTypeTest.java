package com.gypApp_main.modelTest;

import com.gypApp_main.model.TrainingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TrainingTypeTest {

    @Test
    public void testGetterAndSetter() {
        // Arrange
        TrainingType trainingType = new TrainingType();

        // Act
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Cardio");

        // Assert
        assertEquals(1L, trainingType.getId());
        assertEquals("Cardio", trainingType.getTrainingTypeName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        TrainingType type1 = new TrainingType();
        type1.setId(1L);

        TrainingType type2 = new TrainingType();
        type2.setId(1L);

        TrainingType type3 = new TrainingType();
        type3.setId(2L);

        // Assert
        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
        assertEquals(type1.hashCode(), type2.hashCode());
        assertNotEquals(type1.hashCode(), type3.hashCode());
    }



}