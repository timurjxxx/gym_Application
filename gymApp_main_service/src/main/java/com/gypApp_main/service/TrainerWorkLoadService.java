package com.gypApp_main.service;

import com.gypApp_main.dto.TrainerWorkloadRequest;
import com.gypApp_main.model.Training;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.utils.CircuitBreakerManager;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@Slf4j
public class TrainerWorkLoadService implements WorkLoadService {

    private final WebClient.Builder webClientBuilder;
    private final JWTProvider provider;
    private final CircuitBreakerManager circuitBreakerManager;

    public TrainerWorkLoadService(WebClient.Builder webClientBuilder, JWTProvider provider ) {
        this.webClientBuilder = webClientBuilder;
        this.provider = provider;
        this.circuitBreakerManager = new CircuitBreakerManager("TrainerWorkLoadService");
    }


    @CircuitBreaker(name = "TrainerWorkLoadService", fallbackMethod = "updateWorkLoadFallback")
    public void updateWorkLoad(Training training, String action) {

        log.debug("Training details {}", training);
        String token = provider.generateTokenForWorkLoad();
        log.info("Here is toke for trainerworkload {}", token);
        log.info("Action type {}", action);


        TrainerWorkloadRequest response = webClientBuilder.build()
                .post()
                .uri("/updateWorkLoad/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createRequest(training, action)))
                .retrieve()
                .bodyToMono(TrainerWorkloadRequest.class)
                .block();

        log.debug("Response received successfully");
    }

    public TrainerWorkloadRequest createRequest(Training training, String action) {

        String username = training.getTrainer().getUser().getUserName();
        String firstName = training.getTrainer().getUser().getFirstName();
        String lastName = training.getTrainer().getUser().getLastName();
        Boolean isAcitve = training.getTrainer().getUser().getIsActive();
        log.info("Create request with trainer details {}", training);
        log.info("Action type {}", action);

        return TrainerWorkloadRequest.builder()
                .trainerUsername(username)
                .trainerFirstname(firstName)
                .trainerLastname(lastName)
                .isActive(isAcitve)
                .type(action)
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();

    }

    public void updateWorkLoadFallback(Training training, String action, Throwable throwable) {
        circuitBreakerManager.logCircuitBreakerStatus();
        circuitBreakerManager.logCircuitBreakerStatusAndError(throwable);
        Duration duration = Duration.ofMillis(10000);
        CircuitBreakerEvent circuitBreakerEvent = new CircuitBreakerOnErrorEvent("TrainerWorkLoadService", duration,throwable);
        circuitBreakerManager.logCircuitBreakerStatusAndEvent(circuitBreakerEvent);
    }
}

