package com.gypApp_main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gypApp_main.activemq.TrainerWorkloadProducer;
import com.gypApp_main.dao.TrainingDAO;
import com.gypApp_main.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {


    private final TrainingDAO trainingDAO;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    private final TrainerWorkLoadService trainingWorkLoadService;

    private final TrainerWorkloadProducer trainerWorkloadProducer;

    private final TrainingTypeService trainingTypeService;


    @Transactional
    public Training addTraining(Training training, String trainerName, String traineeName, String trainingTypeName) throws JsonProcessingException {
        training.setTrainer(trainerService.selectTrainerByUserName(trainerName));
        training.setTrainee(traineeService.selectTraineeByUserName(traineeName));
        training.setTrainingTypes(trainingTypeService.findByTrainingName(trainingTypeName));
        log.info("Added training with trainer name {}, and trainee name {} and trainingtype {}", trainerName, traineeName, trainingTypeName);
        log.debug("Added training details: {}", training);
        trainingWorkLoadService.updateWorkLoad(training, "ADD");
        return trainingDAO.save(training);
    }

    @Transactional
    public void deleteTraining(String name) throws JsonProcessingException {
        Training training = trainingDAO.getTrainingByTrainingName(name).orElseThrow(() -> new UsernameNotFoundException("Training not found"));
        trainingWorkLoadService.updateWorkLoad(training, "DELETE");
        trainingDAO.deleteTrainingByTrainingName(name);
    }

    public List<Training> getTrainerTrainingsByCriteria(String trainerUsername, TrainingSearchCriteria criteria) {
        Trainer trainer = trainerService.selectTrainerByUserName(trainerUsername);
        log.info("Get trainer with username: {}", trainerUsername);
        log.debug("Trainer details: {}", trainer);
        log.debug("Criteria details: {}", criteria);

        return trainingDAO.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(trainer).ifPresent(t -> predicates.add(cb.equal(root.get("trainer"), t)));

            Optional.ofNullable(criteria).ifPresent(c -> {
                Optional.ofNullable(c.getTrainingName()).ifPresent(v -> predicates.add(cb.equal(root.get("trainingName"), v)));
                if (c.getTrainingStartDate() != null && c.getTrainingEndDate() != null) {
                    predicates.add(cb.between(root.get("trainingDate"), c.getTrainingStartDate(), c.getTrainingEndDate()));
                }
                Optional.ofNullable(c.getTrainingDuration()).ifPresent(v -> predicates.add(cb.equal(root.get("trainingDuration"), v)));

                Optional.ofNullable(c.getTrainingTypes()).ifPresent(trainingType -> {
                    Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
                    predicates.add(cb.equal(trainingTypeJoin.get("trainingTypeName"), trainingType.getTrainingTypeName()));
                });
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }


    public List<Training> getTraineeTrainingsByCriteria(String traineeUsername, TrainingSearchCriteria criteria) {
        Trainee trainee = traineeService.selectTraineeByUserName(traineeUsername);

        log.info("Get trainee with username: {}", traineeUsername);
        log.debug("Trainee details: {}", trainee);
        log.debug("Criterie details: {}", criteria);

        return trainingDAO.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(trainee).ifPresent(t -> predicates.add(cb.equal(root.get("trainee"), t)));

            Optional.ofNullable(criteria).ifPresent(c -> {
                Optional.ofNullable(c.getTrainingName()).ifPresent(v -> predicates.add(cb.equal(root.get("trainingName"), v)));
                if (c.getTrainingStartDate() != null && c.getTrainingEndDate() != null) {
                    predicates.add(cb.between(root.get("trainingDate"), c.getTrainingStartDate(), c.getTrainingEndDate()));
                }
                Optional.ofNullable(c.getTrainingDuration()).ifPresent(v -> predicates.add(cb.equal(root.get("trainingDuration"), v)));

                Optional.ofNullable(c.getTrainingTypes()).ifPresent(trainingType -> {
                    Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType", JoinType.LEFT);
                    predicates.add(cb.equal(trainingTypeJoin.get("trainingTypeName"), trainingType.getTrainingTypeName()));
                });
            });
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }


}