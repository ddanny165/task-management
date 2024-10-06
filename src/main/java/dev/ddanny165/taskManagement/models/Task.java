package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_generator")
    @SequenceGenerator(name = "task_seq_generator", sequenceName = "task_seq", allocationSize = 10)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime toBeDoneUntil;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_employee_user_id")
    private User assignedEmployee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @ManyToMany(mappedBy = "tasks")
    private List<Task> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTask")
    private List<Comment> assignedComments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_project_id")
    private Project assignedProject;
}
