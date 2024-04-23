package com.gypApp_main.dao;

import com.gypApp_main.model.Training;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingDAO extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {
    @EntityGraph(value = "Training.fullGraph", type = EntityGraph.EntityGraphType.FETCH)
    List<Training> findAll(Specification<Training> spec);


    void  deleteTrainingByTrainingName (String name);

    Optional<Training> getTrainingByTrainingName (String  name);



}