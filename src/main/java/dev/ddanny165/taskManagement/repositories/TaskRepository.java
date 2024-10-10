package dev.ddanny165.taskManagement.repositories;

import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;

import java.util.List;

/**
 * Repository for managing {@link Task} entities.
 */
public interface TaskRepository extends AbstractRepository<Task, Long> {
    List<Task> findAllByAssignedProject(Project project);
    List<Task> findAllByAssignedEmployee(Userx userx);
}
