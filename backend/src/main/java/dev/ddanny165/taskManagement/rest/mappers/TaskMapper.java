package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.*;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.services.CommentService;
import dev.ddanny165.taskManagement.services.ProjectService;
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

    private final ProjectService projectService;

    private final CommentService commentService;

    public TaskMapper(TaskService taskService, UserxService userxService,
                      ProjectService projectService, CommentService commentService) {
        this.taskService = taskService;
        this.userxService = userxService;
        this.projectService = projectService;
        this.commentService = commentService;
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

        List<String> assignedTagIds = null;
        if (entity.getTags() != null) {
            assignedTagIds = entity.getTags()
                    .stream()
                    .map(Tag::getName)
                    .toList();
        }

        List<Long> assignedCommentIds = null;
        if (entity.getAssignedComments() != null) {
            assignedCommentIds = entity.getAssignedComments()
                    .stream()
                    .map(Comment::getId)
                    .toList();
        }

        return new TaskDto(entity.getId(), entity.getTitle(), entity.getDescription(), status,
                priority, entity.getCreatedAt(), entity.getToBeDoneUntil(), assignedEmployeeUsername,
                creatorUsername, assignedProjectId, assignedTagIds, assignedCommentIds);
    }

    @Override
    public Task mapFrom(TaskDto dto) {
        if (dto == null) {
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

        Optional<Project> assignedProjectOpt = projectService.findProjectById(dto.assignedProjectId());
        assignedProjectOpt.ifPresent(taskEntity::setAssignedProject);

        List<Comment> assignedComments = new ArrayList<>();
        if (dto.assignedCommentIds() != null && dto.assignedCommentIds().size() != 0) {
            dto.assignedCommentIds().forEach(
                    cid -> {
                        Optional<Comment> foundComment = this.commentService.findCommentById(cid);
                        foundComment.ifPresent(assignedComments::add);
                    }
            );
        }
        taskEntity.setAssignedComments(assignedComments);

        // TODO: set tags

        return taskEntity;
    }
}
