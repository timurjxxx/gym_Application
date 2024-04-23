package com.gypApp_main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerWorkloadRequest {

    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Integer trainingDuration;
    private String type;
}
