package com.gypApp_workLoadService.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "trainers")
@Builder
public class Trainer {

    @MongoId
    @Field("username")
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;

    @Field("workLoad")
    private Map<String, Integer> trainingSummaryDuration;

    @Override
    public String toString() {
        return firstName +" "+lastName+" "+ trainingSummaryDuration;
    }
}



