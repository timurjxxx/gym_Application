package com.gypApp_main.controllerTest;

import com.gypApp_main.controller.TrainingTypeController;
import com.gypApp_main.model.TrainingType;
import com.gypApp_main.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrainingTypes() {
        TrainingType type1 = new TrainingType();
        type1.setId(1L);
        type1.setTrainingTypeName("Type 1");

        TrainingType type2 = new TrainingType();
        type2.setId(2L);
        type2.setTrainingTypeName("Type 2");

        List<TrainingType> trainingTypes = Arrays.asList(type1, type2);

        when(trainingTypeService.getAll()).thenReturn(trainingTypes);

        ResponseEntity<List<TrainingType>> response = trainingTypeController.getTrainingTypes();

        assertEquals(ResponseEntity.ok(trainingTypes), response);
        verify(trainingTypeService, times(1)).getAll();
    }
}