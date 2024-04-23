package com.gypApp_main.actuatorTest;

import com.gypApp_main.actuator.health.BaseHealthCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BaseHealthCheckTest {

    @Mock
    private Exception simulatedException;

    @Spy
    @InjectMocks
    private BaseHealthCheck baseHealthCheck = new BaseHealthCheck() {
        @Override
        public Health checkHealth() throws Exception {
            if (simulatedException != null) {
                throw simulatedException;
            }
            return Health.up().build(); // Change this to Health.up() for success
        }
    };

    @Test
    void health_WhenCheckHealthThrowsException_ShouldReturnDownStatus() throws Exception {
        // Arrange
        simulatedException = new RuntimeException("Simulated error");

        // Act
        Health result = baseHealthCheck.health();

        // Assert
        assertEquals(Status.DOWN, result.getStatus());
        assertEquals("Error during health check", result.getDetails().get("reason"));
        assertEquals("java.lang.RuntimeException: Simulated error", result.getDetails().get("error"));

        // Verify that checkHealth() was called
        verify(baseHealthCheck, times(1)).checkHealth();
    }

    @Test
    void health_WhenCheckHealthSucceeds_ShouldReturnUpStatus() throws Exception {
        // Arrange
        simulatedException = null; // No exception for success case

        // Act
        Health result = baseHealthCheck.health();

        // Assert
        assertEquals(Status.UP, result.getStatus());
        assertEquals(0, result.getDetails().size());

        // Verify that checkHealth() was called
        verify(baseHealthCheck, times(1)).checkHealth();
    }
}