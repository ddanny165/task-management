package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.TaskList;
import dev.ddanny165.taskManagement.models.TaskListVisibility;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.TaskListDto;
import dev.ddanny165.taskManagement.services.TaskListService;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope("application")
public class TaskListMapper implements DTOMapper<TaskList, TaskListDto>{
    private final TaskListService taskListService;

    private final TaskService taskService;

    private final UserxService userxService;

    public TaskListMapper(TaskListService taskListService, UserxService userxService,
                          TaskService taskService) {
        this.taskListService = taskListService;
        this.userxService = userxService;
        this.taskService = taskService;
    }

    @Override
    public TaskListDto mapTo(TaskList entity) {
        if (entity == null) {
            return null;
        }

        List<Long> assignedTaskIds = entity.getTasks().stream()
                .map(Task::getId)
                .toList();

        String creatorUsername = null;
        if (entity.getCreatedBy() != null) {
            creatorUsername = entity.getCreatedBy().getUsername();
        }

        return new TaskListDto(entity.getName(), entity.getVisibility().name(),
                creatorUsername, assignedTaskIds);
    }

    @Override
    public TaskList mapFrom(TaskListDto dto) {
        if (dto == null) {
            return null;
        }

        TaskList taskListEntity = new TaskList();

        taskListEntity.setName(dto.name());
        if (taskListService.isAValidVisibility(dto.visibility())) {
            taskListEntity.setVisibility(TaskListVisibility.valueOf(dto.visibility()));
        }

        Optional<Userx> foundUserxOpt = userxService.findUserById(dto.creatorUsername());
        foundUserxOpt.ifPresent(taskListEntity::setCreatedBy);

        if (dto.assignedTaskIds() != null) {
            taskListEntity.setTasks(
                    dto.assignedTaskIds().stream()
                            .map(tid -> {
                                Optional<Task> foundTaskOpt = this.taskService.findTaskById(tid);
                                return foundTaskOpt.orElse(null);
                            })
                            .filter(task -> task != null)
                            .toList()
            );
        }

        return taskListEntity;
    }
}
