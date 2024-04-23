package com.gypApp_workLoadService.dao;


import com.gypApp_workLoadService.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadDAO extends MongoRepository<Trainer, String> {

    Trainer findTrainerByUsername(String username);

}
