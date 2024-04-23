package com.gypApp_workLoadService.service;

import com.gypApp_workLoadService.dao.TrainerWorkloadDAO;
import com.gypApp_workLoadService.dto.ActionType;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.model.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadDAO trainerWorkloadDAO;

    public void updateWorkload(TrainerWorkloadRequest request) {
        Trainer trainer = trainerWorkloadDAO.findTrainerByUsername(request.getTrainerUsername());
        log.info("Updating trainer work load");
        log.info("Request details {}", request);
        if (trainer == null) {
            trainerWorkloadDAO.save(createNewTrainer(request));
        } else {
            trainerWorkloadDAO.save(updateTrainerWorkLoad(trainer, request));
        }
    }

    Trainer updateTrainerWorkLoad(Trainer trainer, TrainerWorkloadRequest request) {
        String yearAndMonth = getYearAndMonth(trainer.getTrainingSummaryDuration(), request);
        log.info("Update trainer work load ");
        log.info("Trainer details : {}", trainer);
        if (yearAndMonth != null) {
            trainer.getTrainingSummaryDuration().compute(yearAndMonth, (key, currDuration) -> request.getType().equals(ActionType.ADD) ?
                    (currDuration != null ? currDuration : 0) + request.getTrainingDuration() :
                    (currDuration != null ? Math.max(currDuration - request.getTrainingDuration(), 0) : 0));
        } else {
            trainer.getTrainingSummaryDuration().put(generateKeyForTrainingSummaryDuration(request), request.getTrainingDuration());
        }
        return trainer;
    }

    Trainer createNewTrainer(TrainerWorkloadRequest request) {
        log.info("Creating new trainer with details {}", request);
        Map<String, Integer> sum = new HashMap<>();
        String yearAndMonth = generateKeyForTrainingSummaryDuration(request);
        sum.put(yearAndMonth, request.getTrainingDuration());
        log.info("Work load date {}", sum);
        return Trainer.builder()
                .firstName(request.getTrainerFirstname())
                .username(request.getTrainerUsername())
                .lastName(request.getTrainerLastname())
                .isActive(request.getIsActive())
                .trainingSummaryDuration(sum)
                .build();
    }

    String getYearAndMonth(Map<String, Integer> trainingSummaryDuration, TrainerWorkloadRequest request) {
        String yearAndMonth = generateKeyForTrainingSummaryDuration(request);
        log.info("Getting year and month from request : {}", request);
        return trainingSummaryDuration.keySet()
                .stream()
                .filter(key -> key.equals(yearAndMonth))
                .findFirst()
                .orElse(null);
    }

    String generateKeyForTrainingSummaryDuration(TrainerWorkloadRequest request) {
        log.info("Generate date for work load from request :{}", request);
        return request.getTrainingDate().getMonthValue() + ":" + request.getTrainingDate().getYear();
    }


}






