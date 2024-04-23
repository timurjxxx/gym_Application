package com.gypApp_main.actuator.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class NonEmptyTableHealthCheck extends BaseHealthCheck {

    private final DataSource dataSource;
    private final String tableName;

    public NonEmptyTableHealthCheck(DataSource dataSource, @Value("${health.check.table-name:training_type}") String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    @Override
    public Health checkHealth() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT COUNT(*) FROM " + tableName )
            );

            if (resultSet.next()) {
                int nonEmptyTables = resultSet.getInt(1);

                if (nonEmptyTables == 0) {
                    return Health.down().withDetail("reason", "Table '" + tableName + "' is empty").build();
                } else {
                    return Health.up().build();
                }
            } else {
                return Health.down().withDetail("reason", "No rows found for table '" + tableName + "'").build();
            }
        }
    }


}
