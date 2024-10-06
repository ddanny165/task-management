package dev.ddanny165.taskManagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq_generator")
    @SequenceGenerator(name = "tag_seq_generator", sequenceName = "tag_seq", allocationSize = 10)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;
}
