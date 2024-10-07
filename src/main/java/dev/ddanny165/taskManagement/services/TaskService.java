package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.TaskPriority;
import dev.ddanny165.taskManagement.models.TaskStatus;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.TaskRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
     * Updates the task status of a task.
     *
     * @param id of the task to update
     * @param newTaskStatus new task status to set
     */
    public void updateTaskStatus(Long id, TaskStatus newTaskStatus) {
        Optional<Task> foundTask = this.findTaskById(id);

        if (foundTask.isPresent() && !(foundTask.get().getStatus().equals(newTaskStatus))) {
            Task taskToUpdate = foundTask.get();
            taskToUpdate.setStatus(newTaskStatus);
            taskRepository.save(taskToUpdate);
        }
    }

    /**
     * Updates the task priority of a task.
     *
     * @param id of the task to update
     * @param newTaskPriority new task priority to set
     */
    public void updateTaskPriority(Long id, TaskPriority newTaskPriority) {
        Optional<Task> foundTask = this.findTaskById(id);

        if (foundTask.isPresent() && !(foundTask.get().getPriority().equals(newTaskPriority))) {
            Task taskToUpdate = foundTask.get();
            taskToUpdate.setPriority(newTaskPriority);
            taskRepository.save(taskToUpdate);
        }
    }

    public void deleteTask(Long id) {
        Optional<Task> foundTask = this.findTaskById(id);
        // TODO: probably handle external references
        foundTask.ifPresent(this.taskRepository::delete);
    }
}
