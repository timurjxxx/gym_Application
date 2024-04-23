package com.gypApp_main.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CircuitBreakerManager {

    private final CircuitBreaker circuitBreaker;

    public CircuitBreakerManager(String circuitBreakerName) {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        this.registerEventListeners();
    }

    private void registerEventListeners() {
        this.circuitBreaker.getEventPublisher().onSuccess(event -> log.info("Circuit Breaker success event: {}", event));
        this.circuitBreaker.getEventPublisher().onError(event -> log.error("Circuit Breaker error event: {}", event));
        this.circuitBreaker.getEventPublisher().onStateTransition(event -> log.info("Circuit Breaker state transition: {}", event));
    }

    public void logCircuitBreakerStatus() {
        CircuitBreaker.State state = circuitBreaker.getState();
        log.info("Circuit Breaker state: {}", state);
    }

    public void logCircuitBreakerStatusAndEvent(CircuitBreakerEvent event) {
        logCircuitBreakerStatus();
        log.info("Circuit Breaker event: {}", event);
    }

    public void logCircuitBreakerStatusAndError(Throwable throwable) {
        logCircuitBreakerStatus();
        log.error("Circuit Breaker error: {}", throwable.getMessage());
    }
}
