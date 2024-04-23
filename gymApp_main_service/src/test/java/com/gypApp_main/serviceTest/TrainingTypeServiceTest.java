package com.gypApp_main.serviceTest;

import com.gypApp_main.dao.TrainingTypeDAO;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testGetTrainingType() {
        String  name = "test_name";
        TrainingType trainingType = new TrainingType();
        when(trainingTypeDAO.findTrainingTypeByTrainingTypeName(name)).thenReturn(trainingType);

        TrainingType retrievedTrainingType = trainingTypeService.findByTrainingName(name);

        assertNotNull(retrievedTrainingType);
        assertEquals(trainingType, retrievedTrainingType);
        verify(trainingTypeDAO, times(1)).findTrainingTypeByTrainingTypeName(name);
    }
    @Test
    void testGetAllTrainingTypes() {
        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1L);
        trainingType1.setTrainingTypeName("Cardio");

        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2L);
        trainingType2.setTrainingTypeName("Strength");

        List<TrainingType> trainingTypes = Arrays.asList(trainingType1, trainingType2);

        TrainingTypeDAO trainingTypeDAO = mock(TrainingTypeDAO.class);
        when(trainingTypeDAO.findAll()).thenReturn(trainingTypes);

        TrainingTypeService trainingTypeService = new TrainingTypeService(trainingTypeDAO);

        List<TrainingType> result = trainingTypeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(trainingType1.getId(), result.get(0).getId());
        assertEquals(trainingType1.getTrainingTypeName(), result.get(0).getTrainingTypeName());

        assertEquals(trainingType2.getId(), result.get(1).getId());
        assertEquals(trainingType2.getTrainingTypeName(), result.get(1).getTrainingTypeName());

        verify(trainingTypeDAO, times(1)).findAll();
    }

}