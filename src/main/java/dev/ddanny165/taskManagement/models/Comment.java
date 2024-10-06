package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_generator")
    @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
