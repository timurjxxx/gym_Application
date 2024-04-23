package com.gypApp_workLoadService.controller;

import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/updateWorkLoad", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadController {


    private final TrainerWorkloadService trainerWorkloadService;

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateWorkload(@RequestBody TrainerWorkloadRequest request) {
        log.info("Updated trainer work load {}", request);
        log.debug("Action type {}", request.getType());
        trainerWorkloadService.updateWorkload(request);
        return ResponseEntity.ok().build();
    }

}
