package com.gypApp_main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "Trainer.fullGraph",
        attributeNodes = {
                @NamedAttributeNode("trainees"),
                @NamedAttributeNode("traineeTrainings"),
                @NamedAttributeNode("specialization"),
                @NamedAttributeNode(value = "user", subgraph = "user-subgraph")
        },
        subgraphs = @NamedSubgraph(name = "user-subgraph", attributeNodes = {})
)

public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "trainers")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Trainee> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer")
    private List<Training> traineeTrainings;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(id, trainer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trainer{" +"First name=' " + user.getFirstName() + '\''+
                "Last name=' " + user.getLastName() + '\'' +
                "specialization='" + specialization.getTrainingTypeName() + '\'' +
                ",Trainees list =" + (trainees != null ? trainees : 0) +
                '}';
    }
}
