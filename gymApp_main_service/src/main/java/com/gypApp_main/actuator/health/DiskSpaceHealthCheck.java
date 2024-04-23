package com.gypApp_main.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskSpaceHealthCheck extends BaseHealthCheck {

    @Override
    public Health checkHealth() {
        long availableSpace = new File(".").getUsableSpace();
        if (availableSpace < 1024 * 1024 * 1024) { // 1 GB
            return Health.down().withDetail("reason", "Low disk space").build();
        }
        return Health.up().build();
    }
}
