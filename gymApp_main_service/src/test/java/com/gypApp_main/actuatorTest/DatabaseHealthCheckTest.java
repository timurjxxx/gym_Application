package com.gypApp_main.actuatorTest;

import com.gypApp_main.actuator.health.DatabaseHealthCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DatabaseHealthCheckTest {

    @Test
    void checkHealth_WhenConnectionIsValid_ShouldReturnUpStatus() throws SQLException {
        // Arrange
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(0)).thenReturn(true);

        DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(mockDataSource);

        // Act
        Health result = databaseHealthCheck.checkHealth();

        // Assert
        assertEquals(Status.UP, result.getStatus());
    }

    @Test
    void checkHealth_WhenConnectionIsNotValid_ShouldReturnDownStatus() throws SQLException {
        // Arrange
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(0)).thenReturn(false);

        DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(mockDataSource);

        // Act
        Health result = databaseHealthCheck.checkHealth();

        // Assert
        assertEquals(Status.DOWN, result.getStatus());
        assertEquals("Database connection not valid", result.getDetails().get("reason"));
    }

}