package com.gypApp_main.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class DatabaseMetrics {

    @Autowired
    public DataSource dataSource;

    private final Counter failedQueries;
    private final Timer connectionTimer;

    @Autowired
    public DatabaseMetrics(MeterRegistry registry) {
        this.failedQueries = Counter.builder("database_failed_queries")
                .description("Number of failed database queries")
                .register(registry);

        this.connectionTimer = Timer.builder("database_connection_time")
                .description("Time taken to acquire a database connection")
                .register(registry);
    }


    @PostConstruct
    public void initMetrics() {
        try (Connection connection = dataSource.getConnection()) {
            connectionTimer.record(() -> {
            });
        } catch (SQLException e) {
            failedQueries.increment();
        }
    }
}
