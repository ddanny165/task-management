package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.UserxRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class UserxService {
    private final UserxRepository userxRepository;

    public UserxService(UserxRepository userxRepository) {
        this.userxRepository = userxRepository;
    }

    public List<Userx> findAllUsers() {
        return userxRepository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An Optional containing the user if found, otherwise an empty Optional.
     */
    public Optional<Userx> findUserById(String id) {
        return userxRepository.findById(id);
    }

    /**
     * Saves the user.
     *
     * @param user the user to save
     * @return the updated user
     */
    public Userx saveUser(Userx user) {
        return userxRepository.save(user);
    }

    /**
     * Checks whether a user with a provided username already exists.
     * True is returned when the user with the provided username already exists,
     * False is returned otherwise.
     *
     * @param username to check
     * @return boolean, that says whether exists.
     */
    public boolean doesAlreadyExistWithSuchUsername(String username) {
        return this.findUserById(username).isPresent();
    }
}
