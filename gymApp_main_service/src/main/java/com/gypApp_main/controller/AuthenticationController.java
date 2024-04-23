package com.gypApp_main.controller;

import com.gypApp_main.dto.ChangeLoginRequest;
import com.gypApp_main.dto.LoginRequest;
import com.gypApp_main.model.Trainee;
import com.gypApp_main.model.Trainer;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthService service;
    private final JWTProvider provider;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login request received for username: {}", request.getUsername());
        log.debug("Login request details: {}", request);
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping(value = "/create_trainee", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTrainee(@RequestBody Trainee trainee) {
        log.info("Creating trainee with username: {}", trainee.getUser().getUserName());
        log.debug("Trainee details: {}", trainee);
        return ResponseEntity.ok(service.createTrainee(trainee));
    }

    @PostMapping(value = "/create_trainer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTrainer(@RequestBody Trainer trainer) {
        log.info("Creating trainer with username: {}", trainer.getUser().getUserName());
        log.debug("Trainer details: {}", trainer);
        return ResponseEntity.ok(service.createTrainer(trainer));
    }

    @PutMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeLogin(@RequestBody ChangeLoginRequest request) {
        log.info("Change password request received for username: {}", request.getUsername());
        log.debug("Change password request details: {}", request);
        return ResponseEntity.ok(service.changePassword(request));
    }
}
