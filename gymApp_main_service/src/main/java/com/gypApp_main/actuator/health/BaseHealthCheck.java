package com.gypApp_main.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public abstract class BaseHealthCheck implements HealthIndicator {

    @Override
    public final Health health() {
        try {
            return checkHealth();
        } catch (Exception e) {
            return Health.down(e).withDetail("reason", "Error during health check").build();
        }
    }

    public abstract Health checkHealth() throws Exception;
}
