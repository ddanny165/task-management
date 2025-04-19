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
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Comment implements Persistable<Long>, Serializable, Comparable<Comment>  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_generator")
    @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Userx creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task assignedTask;

    public Comment(String text, LocalDateTime createdAt, Userx creator, Task assignedTask) {
        this.text = text;
        this.createdAt = createdAt;
        this.creator = creator;
        this.assignedTask = assignedTask;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignedTask, creator, createdAt, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Comment other)) {
            return false;
        }

        return Objects.equals(this.assignedTask, other.assignedTask)
                && Objects.equals(this.creator, other.creator)
                && Objects.equals(this.createdAt, other.createdAt)
                && Objects.equals(this.text, other.text);
    }

    @Override
    public int compareTo(Comment other) {
        int resultAssignedTask = this.assignedTask.compareTo(other.assignedTask);
        if (resultAssignedTask != 0) {
            return resultAssignedTask;
        }

        int resultCreator = this.creator.compareTo(other.creator);
        if (resultCreator != 0) {
            return resultCreator;
        }

        int resultCreatedAt = this.createdAt.compareTo(other.createdAt);
        if (resultCreatedAt != 0) {
            return resultCreatedAt;
        }

        return this.text.compareTo(other.text);
    }
}
