package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.*;
import dev.ddanny165.taskManagement.repositories.TaskListRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("application")
public class TaskListService {
    private final TaskListRepository taskListRepository;

    private UserxService userxService;

    public TaskListService(TaskListRepository taskListRepository, UserxService userxService) {
        this.taskListRepository = taskListRepository;
        this.userxService = userxService;
    }

    public Optional<TaskList> findTaskListById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.taskListRepository.findById(id);
    }

    public List<TaskList> findAllTaskListsByUserxUsername(String username) {
        Optional<Userx> foundUserxOpt = userxService.findUserById(username);
        if (foundUserxOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Userx foundUserx = foundUserxOpt.get();
        return foundUserx.getCreatedTaskLists();
    }

    public boolean isAValidVisibility(String visibility) {
        if (visibility == null || visibility.length() == 0) {
            return false;
        }

        return new HashSet<>(Arrays.stream(TaskListVisibility.values())
                .map(TaskListVisibility::name)
                .toList())
                .contains(visibility);
    }
}
