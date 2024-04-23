package com.gypApp_main.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class DatabaseHealthCheck  extends BaseHealthCheck  {
    private final DataSource dataSource;

    public DatabaseHealthCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health checkHealth() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(0)) {
                return Health.down().withDetail("reason", "Database connection not valid").build();
            }
            return Health.up().build();
        }
    }
}