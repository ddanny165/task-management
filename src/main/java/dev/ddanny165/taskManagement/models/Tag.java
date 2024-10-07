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
public class Tag implements Persistable<String>, Serializable, Comparable<Tag> {
    @Id
    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;

    public Tag(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public String getId() {
        return name;
    }

    // TODO: check the correctness of using an id as a significant variable
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Tag other)) {
            return false;
        }

        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(Tag other) {
        return this.name.compareTo(other.name);
    }
}
