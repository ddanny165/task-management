package dev.ddanny165.taskManagement.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
