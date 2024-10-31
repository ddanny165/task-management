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

    private final UserxService userxService;

    public TaskListService(TaskListRepository taskListRepository, UserxService userxService) {
        this.taskListRepository = taskListRepository;
        this.userxService = userxService;
    }

    public List<TaskList> findAllTaskLists() {
        return taskListRepository.findAll();
    }

    public Optional<TaskList> findTaskListById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.taskListRepository.findById(id);
    }

    public TaskList saveTaskList(TaskList taskList) {
        if (taskList == null) {
            return null;
        }

        return taskListRepository.save(taskList);
    }

    public List<TaskList> findAllTaskListsByUserxUsername(String username) {
        Optional<Userx> foundUserxOpt = userxService.findUserById(username);
        if (foundUserxOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Userx foundUserx = foundUserxOpt.get();
        return foundUserx.getCreatedTaskLists();
    }

    public Optional<TaskList> updateTaskList(Long id, TaskList updateData) {
        if (updateData == null || id == null) {
            return Optional.empty();
        }

        Optional<TaskList> foundTaskListOpt = taskListRepository.findById(id);
        if (foundTaskListOpt.isEmpty()) {
            return Optional.empty();
        }

        TaskList foundTaskList = foundTaskListOpt.get();

        if (updateData.getName() != null) {
            foundTaskList.setName(updateData.getName());
        }

        if (updateData.getVisibility() != null) {
            foundTaskList.setVisibility(updateData.getVisibility());
        }

        if (updateData.getTasks() != null) {
            List<Task> assignedTasks = new ArrayList<>();
            updateData.getTasks().forEach(t -> assignedTasks.add(t));
            foundTaskList.setTasks(assignedTasks);
        }

        TaskList updatedTaskList = taskListRepository.save(foundTaskList);
        return Optional.of(updatedTaskList);
    }

    public void deleteTaskList(Long id) {
        if (id != null) {
            Optional<TaskList> foundTaskList= this.findTaskListById(id);
            foundTaskList.ifPresent(this.taskListRepository::delete);
        }
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
