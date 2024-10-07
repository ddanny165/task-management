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
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String username;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "second_name", length = 50, nullable = false)
    private String secondName;

    @Column(length = 50, nullable = false)
    private String email;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "User_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Comment> createdComments;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "assignedEmployees")
    private List<Project> assignedProjects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedUserx", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedEmployee")
    private List<Task> assignedTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Task> createdTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<TaskList> createdTaskLists;

    public Userx(String username, String password, String firstName, String secondName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;

        this.roles = new HashSet<>();
        this.createdComments = new ArrayList<>();
        this.assignedProjects = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.assignedTasks = new ArrayList<>();
        this.createdTasks = new ArrayList<>();
        this.createdTaskLists = new ArrayList<>();
    }

    @Override
    public boolean isNew() {
        return (createDate == null);
    }

    @Override
    public String getId() {
        return username;
    }

    @Override
    public int compareTo(Userx other) {
        return this.username.compareTo(other.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Userx other)) {
            return false;
        }

        if (!Objects.equals(this.username, other.username)) {
            return false;
        }

        return true;
    }
}
