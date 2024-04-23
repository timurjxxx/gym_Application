package com.gypApp_main.actuatorTest;

import com.gypApp_main.actuator.health.NonEmptyTableHealthCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class NonEmptyTableHealthCheckTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private NonEmptyTableHealthCheck nonEmptyTableHealthCheck;
    @Test
    void checkHealth_WhenTableIsEmpty_ShouldReturnDownStatus() throws SQLException {
        String tableName = "training_type"; // Set the table name

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        // Act
        NonEmptyTableHealthCheck nonEmptyTableHealthCheck = new NonEmptyTableHealthCheck(dataSource, tableName);
        Health result = nonEmptyTableHealthCheck.checkHealth();

        // Assert
        assertEquals(Status.DOWN, result.getStatus());
        assertEquals("Table 'training_type' is empty", result.getDetails().get("reason"));

        // Verify interactions
        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).createStatement();
        verify(statement, times(1)).executeQuery(anyString());
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt(1);
    }

    @Test
    void checkHealth_WhenTableIsNotEmpty_ShouldReturnUpStatus() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Act
        Health result = nonEmptyTableHealthCheck.checkHealth();

        // Assert
        assertEquals(Status.UP, result.getStatus());
        assertEquals(0, result.getDetails().size());

        // Verify interactions
        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).createStatement();
        verify(statement, times(1)).executeQuery(anyString());
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt(1);
    }


}