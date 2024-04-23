package com.gypApp_main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSearchCriteria   {

    private String trainingName;
    private LocalDate trainingStartDate;
    private LocalDate trainingEndDate;
    private Integer trainingDuration;

    private TrainingType trainingTypes;


}

