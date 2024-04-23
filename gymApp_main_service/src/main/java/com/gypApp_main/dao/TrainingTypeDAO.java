package com.gypApp_main.dao;

import com.gypApp_main.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeDAO extends JpaRepository<TrainingType, Long> {


    TrainingType findTrainingTypeByTrainingTypeName(String name);

}
