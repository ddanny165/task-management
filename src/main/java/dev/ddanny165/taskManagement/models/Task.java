package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Task implements Persistable<Long>, Serializable, Comparable<Task> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_generator")
    @SequenceGenerator(name = "task_seq_generator", sequenceName = "task_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_project_id")
    private Project assignedProject;

    @ManyToMany(mappedBy = "tasks")
    private List<Task> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTask")
    private List<Comment> assignedComments;

    public Task(String title, String description, TaskStatus status, TaskPriority priority,
                LocalDateTime createdAt, LocalDateTime toBeDoneUntil, User assignedEmployee, User createdBy,
                Project assignedProject) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.toBeDoneUntil = toBeDoneUntil;
        this.assignedEmployee = assignedEmployee;
        this.createdBy = createdBy;
        this.assignedProject = assignedProject;

        this.tags = new ArrayList<>();
        this.assignedComments = new ArrayList<>();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    // TODO: check the correctness of using an id as a significant variable
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Task other)) {
            return false;
        }

        return Objects.equals(this.id, other.id);
    }

    @Override
    public int compareTo(Task other) {
        return this.id.compareTo(other.id);
    }
}
