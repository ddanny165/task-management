package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.TaskPriority;
import dev.ddanny165.taskManagement.models.TaskStatus;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.TaskRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class TaskService {
    private final TaskRepository taskRepository;

    private final UserxService userxService;

    public TaskService(TaskRepository taskRepository, UserxService userxService) {
        this.taskRepository = taskRepository;
        this.userxService = userxService;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The unique identifier of the task.
     * @return An Optional containing the task if found, otherwise an empty Optional.
     */
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Saves the task.
     *
     * @param task the task to save
     * @return the updated task
     */
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates the task for more than one attribute being updated
     *
     * @param id of the task to update
     * @param updateData to update the task with
     * @return Optional, specifying whether the task was updated successfully or not
     */
    public Optional<Task> updateTask(Long id, Task updateData) {
        Optional<Task> foundTaskOpt = taskRepository.findById(id);
        if (foundTaskOpt.isEmpty()) {
            return Optional.empty();
        }

        Task foundTask = foundTaskOpt.get();

        if (updateData.getTitle() != null) {
            foundTask.setTitle(updateData.getTitle());
        }

        if (updateData.getDescription() != null) {
            foundTask.setDescription(updateData.getDescription());
        }

        if (updateData.getStatus() != null) {
            foundTask.setStatus(updateData.getStatus());
        }

        if (updateData.getPriority() != null) {
            foundTask.setPriority(updateData.getPriority());
        }

        if (updateData.getCreatedAt() != null) {
            foundTask.setCreatedAt(updateData.getCreatedAt());
        }

        if (updateData.getToBeDoneUntil() != null) {
            foundTask.setToBeDoneUntil(updateData.getToBeDoneUntil());
        }

        if (updateData.getAssignedEmployee() != null) {
            foundTask.setAssignedEmployee(updateData.getAssignedEmployee());
        }

        if (updateData.getAssignedProject() != null) {
            foundTask.setAssignedProject(updateData.getAssignedProject());
        }

        if (updateData.getTags() != null) {
            foundTask.setTags(updateData.getTags());
        }

        if (updateData.getAssignedComments() != null) {
            foundTask.setAssignedComments(updateData.getAssignedComments());
        }

        return Optional.of(foundTask);
     }

    public void deleteTask(Long id) {
        Optional<Task> foundTask = this.findTaskById(id);
        // TODO: probably handle external references
        foundTask.ifPresent(this.taskRepository::delete);
    }

    /**
     * Updates the task status of a task.
     *
     * @param id of the task to update
     * @param newTaskStatus new task status to set
     * @return Optional, indicating whether an update occurred (not empty optional) or not (empty optional)
     */
    public Optional<Task> updateTaskStatus(Long id, TaskStatus newTaskStatus) {
        Optional<Task> foundTask = this.findTaskById(id);
        Optional<Task> updatedTask = Optional.empty();

        if (foundTask.isPresent()) {
            Task taskToUpdate = foundTask.get();
            taskToUpdate.setStatus(newTaskStatus);
            updatedTask = Optional.of(taskRepository.save(taskToUpdate));
        }

        return updatedTask;
    }

    /**
     * Updates the priority level of a task.
     *
     * @param id of the task to update
     * @param taskPriority new task priority to set
     * @return Optional, indicating whether an update occurred (not empty optional) or not (empty optional)
     */
    public Optional<Task> updateTaskPriority(Long id, TaskPriority taskPriority) {
        Optional<Task> foundTask = this.findTaskById(id);
        Optional<Task> updatedTask = Optional.empty();

        if (foundTask.isPresent()) {
            Task taskToUpdate = foundTask.get();
            taskToUpdate.setPriority(taskPriority);
            updatedTask = Optional.of(taskRepository.save(taskToUpdate));
        }

        return updatedTask;
    }

    /**
     * Updates the deadline of a task.
     *
     * @param id of the task to update
     * @param toBeDoneUntil deadline to set
     * @return Optional, indicating whether an update occurred (not empty optional) or not (empty optional)
     */
    public Optional<Task> updateTaskDeadline(Long id, LocalDateTime toBeDoneUntil) {
        Optional<Task> foundTask = this.findTaskById(id);
        Optional<Task> updatedTask = Optional.empty();

        if (foundTask.isPresent()) {
            Task taskToUpdate = foundTask.get();
            taskToUpdate.setToBeDoneUntil(toBeDoneUntil);
            updatedTask = Optional.of(taskRepository.save(taskToUpdate));
        }

        return updatedTask;
    }

    /**
     * Assigns the user to the task
     *
     * @param taskID of the task to update
     * @param userID new task priority to set
     * @return Optional, indicating whether an update occurred (not empty optional) or not (empty optional)
     */
    public Optional<Task> assignTaskToUser(Long taskID, String userID) {
        Optional<Task> foundTask = this.findTaskById(taskID);
        Optional<Userx> foundUser = this.userxService.findUserById(userID);

        Optional<Task> updatedTask = Optional.empty();

        if (foundTask.isPresent() && foundUser.isPresent()) {
            Task taskToUpdate = foundTask.get();
            Userx userToAssign = foundUser.get();

            taskToUpdate.setAssignedEmployee(userToAssign);
            userToAssign.getAssignedTasks().add(taskToUpdate);

            updatedTask = Optional.of(taskRepository.save(taskToUpdate));
        }

        return updatedTask;
    }

    public boolean isAValidStatus(String taskStatus) {
        if (taskStatus == null || taskStatus.length() == 0) {
            return false;
        }

        return new HashSet<>(Arrays.stream(TaskStatus.values())
                .map(TaskStatus::name)
                .toList()).contains(taskStatus);
    }

    public boolean isAValidPriority(String taskPriority) {
        if (taskPriority == null || taskPriority.length() == 0) {
            return false;
        }

        return new HashSet<>(Arrays.stream(TaskPriority.values())
                .map(TaskPriority::name)
                .toList()).contains(taskPriority);
    }
}
