package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskList_seq_generator")
    @SequenceGenerator(name = "taskList_seq_generator", sequenceName = "taskList_seq", allocationSize = 10)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TaskListVisibility visibility;
}
