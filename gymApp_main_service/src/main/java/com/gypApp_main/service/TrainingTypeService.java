package com.gypApp_main.service;

import com.gypApp_main.dao.TrainingTypeDAO;
import com.gypApp_main.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainingTypeService(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    public TrainingType findByTrainingName(String name) {
        return trainingTypeDAO.findTrainingTypeByTrainingTypeName(name);
    }
    public List<TrainingType> getAll() {
        return trainingTypeDAO.findAll();
    }
}

