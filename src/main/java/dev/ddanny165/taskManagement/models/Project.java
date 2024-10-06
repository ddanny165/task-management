package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_generator")
    @SequenceGenerator(name = "project_seq_generator", sequenceName = "project_seq", allocationSize = 10)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}
