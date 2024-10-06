package dev.ddanny165.taskManagement.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @Column(length = 100)
    private String username;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedUser", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedEmployee")
    private List<Task> assignedTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Task> createdTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<TaskList> createdTaskLists;
}
