package dev.ddanny165.taskManagement.repositories;

import dev.ddanny165.taskManagement.models.TaskList;
import dev.ddanny165.taskManagement.models.TaskListVisibility;

import java.util.List;

/**
 * Repository for managing {@link TaskList} entities.
 */
public interface TaskListRepository extends AbstractRepository<TaskList, Long> {
    List<TaskList> findAllByVisibility(TaskListVisibility taskListVisibility);
}
