package com.gypApp_main.actuatorTest;

import com.gypApp_main.actuator.metrics.JvmMemoryMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JvmMemoryMetricsTest {

    @Test
    public void testJvmMemoryMetrics() {
        // Arrange
        MeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        JvmMemoryMetrics jvmMemoryMetrics = new JvmMemoryMetrics();

        // Act
        double memoryUsageValue = jvmMemoryMetrics.getMemoryUsage().value();

        // Assert
        assertEquals(Runtime.getRuntime().totalMemory(), memoryUsageValue, "Memory usage should match total memory");
    }
}