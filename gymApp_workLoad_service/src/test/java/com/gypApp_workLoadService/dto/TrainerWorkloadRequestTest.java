package com.gypApp_workLoadService.dto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainerWorkloadRequestTest {

    @Test
    void testGetterSetter() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("john_doe");
        request.setTrainerFirstname("John");
        request.setTrainerLastname("Doe");
        request.setIsActive(true);
        request.setTrainingDate(LocalDate.of(2022, 4, 4));
        request.setTrainingDuration(60);
        request.setType(ActionType.ADD);

        assertEquals("john_doe", request.getTrainerUsername());
        assertEquals("John", request.getTrainerFirstname());
        assertEquals("Doe", request.getTrainerLastname());
        assertEquals(true, request.getIsActive());
        assertEquals(LocalDate.of(2022, 4, 4), request.getTrainingDate());
        assertEquals(60, request.getTrainingDuration());
        assertEquals(ActionType.ADD, request.getType());
    }

    @Test
    void testBuilder() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2022, 4, 4))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        assertNotNull(request);
        assertEquals("john_doe", request.getTrainerUsername());
        assertEquals("John", request.getTrainerFirstname());
        assertEquals("Doe", request.getTrainerLastname());
        assertEquals(true, request.getIsActive());
        assertEquals(LocalDate.of(2022, 4, 4), request.getTrainingDate());
        assertEquals(60, request.getTrainingDuration());
        assertEquals(ActionType.ADD, request.getType());
    }

    @Test
    void testToString() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john_doe")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2022, 4, 4))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        String expectedToString = "TrainerWorkloadRequest(trainerUsername=john_doe, trainerFirstname=John, " +
                "trainerLastname=Doe, isActive=true, trainingDate=2022-04-04, trainingDuration=60, type=ADD)";

        assertEquals(expectedToString, request.toString());
    }

    @Test
    void testEquals() {
        TrainerWorkloadRequest request1 = TrainerWorkloadRequest.builder()
                .trainerUsername("username")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        TrainerWorkloadRequest request2 = TrainerWorkloadRequest.builder()
                .trainerUsername("username")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        TrainerWorkloadRequest request3 = TrainerWorkloadRequest.builder()
                .trainerUsername("anotherUsername")
                .trainerFirstname("Jane")
                .trainerLastname("Doe")
                .isActive(false)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(90)
                .type(ActionType.DELETE)
                .build();

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
    }
    @Test
    void testHashCode() {
        TrainerWorkloadRequest request1 = TrainerWorkloadRequest.builder()
                .trainerUsername("username")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        TrainerWorkloadRequest request2 = TrainerWorkloadRequest.builder()
                .trainerUsername("username")
                .trainerFirstname("John")
                .trainerLastname("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(60)
                .type(ActionType.ADD)
                .build();

        TrainerWorkloadRequest request3 = TrainerWorkloadRequest.builder()
                .trainerUsername("anotherUsername")
                .trainerFirstname("Jane")
                .trainerLastname("Doe")
                .isActive(false)
                .trainingDate(LocalDate.of(2024, 4, 3))
                .trainingDuration(90)
                .type(ActionType.DELETE)
                .build();

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
}
