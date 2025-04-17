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
public class Notification implements Persistable<Long>, Serializable, Comparable<Notification> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq_generator")
    @SequenceGenerator(name = "notification_seq_generator", sequenceName = "notification_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    private String message;

    private Boolean isRead;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Userx assignedUserx;

    @OneToOne
    @JoinColumn(name = "assigned_task_id")
    private Task assignedTask;

    public Notification(String message, Boolean isRead, LocalDateTime createdAt, Userx assignedUserx, Task assignedTask) {
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.assignedUserx = assignedUserx;
        this.assignedTask = assignedTask;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, createdAt, assignedUserx, assignedTask);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Notification other)) {
            return false;
        }

        return Objects.equals(this.message, other.message) && Objects.equals(this.createdAt, other.createdAt)
                && Objects.equals(this.assignedUserx, other.assignedUserx)
                && Objects.equals(this.assignedTask, other.assignedTask);
    }

    @Override
    public int compareTo(Notification other) {
        int resultMessage = this.message.compareTo(other.message);
        if (resultMessage != 0) {
            return resultMessage;
        }

        int resultCreatedAt = this.createdAt.compareTo(other.createdAt);
        if (resultCreatedAt != 0) {
            return resultCreatedAt;
        }

        int resultAssignedUser = this.assignedUserx.compareTo(other.assignedUserx);
        if (resultAssignedUser != 0) {
            return resultAssignedUser;
        }

        return this.assignedTask.compareTo(other.assignedTask);
    }
}
