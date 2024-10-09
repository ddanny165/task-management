package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.*;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

        String priority = null;
        if (entity.getPriority() != null) {
            priority = entity.getPriority().name();
        }

        String assignedEmployeeUsername = null;
        if (entity.getAssignedEmployee() != null) {
            assignedEmployeeUsername = entity.getAssignedEmployee().getUsername();
        }

        String creatorUsername = null;
        if (entity.getCreatedBy() != null) {
            creatorUsername = entity.getCreatedBy().getUsername();
        }

        Long assignedProjectId = null;
        if (entity.getAssignedProject() != null) {
            assignedProjectId = entity.getAssignedProject().getId();
        }

        List<String> assignedTagIds = entity.getTags()
                .stream()
                .map(Tag::getName)
                .toList();

        List<Long> assignedCommentIds = entity.getAssignedComments()
                .stream()
                .map(Comment::getId)
                .toList();

        return new TaskDto(entity.getId(), entity.getTitle(), entity.getDescription(), status,
                priority, entity.getCreatedAt(), entity.getToBeDoneUntil(), assignedEmployeeUsername,
                creatorUsername, assignedProjectId, assignedTagIds, assignedCommentIds);
    }

    @Override
    public Task mapFrom(TaskDto dto) {
        if (dto == null || dto.id() == null) {
            return null;
        }

        Task taskEntity = new Task();

        taskEntity.setTitle(dto.title());
        taskEntity.setDescription(dto.description());

        if (taskService.isAValidStatus(dto.status())) {
            taskEntity.setStatus(TaskStatus.valueOf(dto.status()));
        }

        if (taskService.isAValidPriority(dto.priority())) {
            taskEntity.setPriority(TaskPriority.valueOf(dto.priority()));
        }

        taskEntity.setCreatedAt(dto.createdAt());
        taskEntity.setToBeDoneUntil(dto.toBeDoneUntil());

        Optional<Userx> assignedUserOpt = userxService.findUserById(dto.assignedEmployeeUsername());
        assignedUserOpt.ifPresent(taskEntity::setAssignedEmployee);

        Optional<Userx> creatorOpt = userxService.findUserById(dto.creatorUsername());
        creatorOpt.ifPresent(taskEntity::setCreatedBy);

        // TODO: set Project
        // Optional<Project> assignedProjectOpt = projectService.findProjectById(dto.assignedProjectId());
        // assignedProjectOpt.ifPresent(foundTask::setAssignedProject);

        // TODO: set comments
        // TODO: set tags

        return taskEntity;
    }
}
