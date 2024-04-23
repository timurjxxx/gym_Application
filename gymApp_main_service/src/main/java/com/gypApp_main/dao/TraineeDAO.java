package com.gypApp_main.dao;

import com.gypApp_main.model.Trainee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeDAO extends JpaRepository<Trainee, Long> {

    @EntityGraph(value = "Trainee.fullGraph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Trainee> findTraineeByUserUserName(String username);

    void deleteTraineeByUserUserName(String username);

}