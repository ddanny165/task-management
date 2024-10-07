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
public class TaskList implements Persistable<Long>, Serializable, Comparable<TaskList> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskList_seq_generator")
    @SequenceGenerator(name = "taskList_seq_generator", sequenceName = "taskList_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TaskListVisibility visibility;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Task> tasks;

    public TaskList(String name, TaskListVisibility visibility, User creator) {
        this.name = name;
        this.visibility = visibility;
        this.createdBy = creator;

        this.tasks = new ArrayList<>();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, createdBy);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TaskList other)) {
            return false;
        }

        return Objects.equals(this.name, other.name) && Objects.equals(this.createdBy, other.createdBy);
    }

    @Override
    public int compareTo(TaskList other) {
        int resultName = this.name.compareTo(other.name);
        if (resultName != 0) {
            return resultName;
        }

        return this.createdBy.compareTo(other.createdBy);
    }
}
