package com.gypApp_main.actuatorTest;

import com.gypApp_main.actuator.health.DiskSpaceHealthCheck;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiskSpaceHealthCheckTest {


    @Test
    public void testCheckHealthUp() {
        // Arrange
        DiskSpaceHealthCheck healthCheck = new DiskSpaceHealthCheck();

        // Act
        Health result = healthCheck.checkHealth();

        // Assert
        assertEquals(Health.up().build(), result, "Health check should be UP when disk space is sufficient");
    }


}