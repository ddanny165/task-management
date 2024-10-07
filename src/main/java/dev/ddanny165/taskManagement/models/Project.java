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
public class Project implements Persistable<Long>, Serializable, Comparable<Project> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_generator")
    @SequenceGenerator(name = "project_seq_generator", sequenceName = "project_seq", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable( name =  "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Userx> assignedEmployees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedProject")
    private List<Task> assignedTasks;

    public Project(String name) {
        this.name = name;

        this.assignedEmployees = new ArrayList<>();
        this.assignedTasks = new ArrayList<>();
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

        if (!(obj instanceof Project other)) {
            return false;
        }

        return Objects.equals(this.id, other.id);
    }

    @Override
    public int compareTo(Project other) {
        return this.id.compareTo(other.id);
    }
}
