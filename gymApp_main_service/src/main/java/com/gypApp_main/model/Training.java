package com.gypApp_main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "Training.fullGraph",
        attributeNodes = {
                @NamedAttributeNode("trainingTypes")
        }
)
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Training name cannot be blank")
    @Column(nullable = false)
    private String trainingName;

    @NotNull(message = "Training date cannot be null")
    @Future(message = "Training date must be in the future")
    @Column(nullable = false)
    private LocalDate trainingDate;

    @NotNull(message = "Training duration cannot be null")
    @Positive(message = "Training duration must be a positive number")
    @Column(nullable = false, columnDefinition = "NUMERIC")
    private Integer trainingDuration;

    @ManyToOne
//    @JsonIgnore  // Исключить поле из процесса сериализации
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingTypes;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                ", trainingTypes=" + (trainingTypes != null ? trainingTypes.getTrainingTypeName() : null) +
                ", trainee=" + (trainee != null ? trainee.getUser().getUserName() : null) +
                ", trainer=" + (trainer != null ? trainer.getUser().getUserName() : null) +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(id, training.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
