package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq_generator")
    @SequenceGenerator(name = "notification_seq_generator", sequenceName = "notification_seq", allocationSize = 10)
    private Long id;

    private String message;

    private Boolean isRead;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @OneToOne
    @JoinColumn(name = "assigned_task_id")
    private Task assignedTask;
}
