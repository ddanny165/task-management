package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.TaskPriority;
import dev.ddanny165.taskManagement.models.TaskStatus;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Component
@Scope("application")
public class TaskMapper implements DTOMapper<Task, TaskDto> {
    private final TaskService taskService;

    private final UserxService userxService;

    public TaskMapper(TaskService taskService, UserxService userxService) {
        this.taskService = taskService;
        this.userxService = userxService;
    }

    @Override
    public TaskDto mapTo(Task entity) {
        if (entity == null) {
            return null;
        }

        String status = null;
        if (entity.getStatus() != null) {
            status = entity.getStatus().name();
        }
        String assignedEmployeeUsername = null;
        if (entity.getAssignedEmployee() != null) {
            assignedEmployeeUsername = entity.getAssignedEmployee().getUsername();
        }

        return new TaskDto(entity.getId(), entity.getTitle(), entity.getDescription(), status,
                entity.getPriority().name(), entity.getToBeDoneUntil(), assignedEmployeeUsername);
    }

    @Override
    public Task mapFrom(TaskDto dto) {
        if (dto == null || dto.id() == null) {
            return null;
        }

        Optional<Task> foundTaskOpt = taskService.findTaskById(dto.id());
        if (foundTaskOpt.isEmpty()) {
            return null;
        }

        Task foundTask = foundTaskOpt.get();
        foundTask.setTitle(dto.title());
        foundTask.setDescription(dto.description());

        if (isAValidStatus(dto.status())) {
            foundTask.setStatus(TaskStatus.valueOf(dto.status()));
        }

        if (isAValidPriority(dto.priority())) {
            foundTask.setPriority(TaskPriority.valueOf(dto.priority()));
        }

        foundTask.setToBeDoneUntil(dto.toBeDoneUntil());

        Optional<Userx> assignedUserOpt = userxService.findUserById(dto.assignedEmployeeUsername());
        assignedUserOpt.ifPresent(foundTask::setAssignedEmployee);

        return foundTask;
    }

    private boolean isAValidStatus(String taskStatus) {
        return new HashSet<>(Arrays.stream(TaskStatus.values())
                .map(TaskStatus::name)
                .toList()).contains(taskStatus);
    }

    private boolean isAValidPriority(String taskPriority) {
        return new HashSet<>(Arrays.stream(TaskPriority.values())
                .map(TaskPriority::name)
                .toList()).contains(taskPriority);
    }
}
