package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "assignedProjects")
    private List<User> assignedEmployees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedProject")
    private List<Task> assignedTasks;
}
