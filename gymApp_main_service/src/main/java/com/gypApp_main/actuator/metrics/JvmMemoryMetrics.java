package com.gypApp_main.actuator.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class JvmMemoryMetrics {

    private final Gauge memoryUsage;

    public JvmMemoryMetrics() {
        MeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        memoryUsage = Gauge.builder("jvm_memory_usage_bytes", () -> Runtime.getRuntime().totalMemory())
                .description("Current memory usage of the JVM in bytes")
                .baseUnit("bytes")
                .register(registry);
    }

    public Gauge getMemoryUsage() {
        return memoryUsage;
    }
}
