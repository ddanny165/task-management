package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.Comment;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.CommentDto;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("application")
public class CommentMapper implements DTOMapper<Comment, CommentDto> {
    private final UserxService userxService;
    private final TaskService taskService;

    public CommentMapper(UserxService userxService, TaskService taskService) {
        this.userxService = userxService;
        this.taskService = taskService;
    }

    @Override
    public CommentDto mapTo(Comment entity) {
        if (entity == null) {
            return null;
        }

        String creatorUsername = null;
        if (entity.getCreator() != null) {
            creatorUsername = entity.getCreator().getUsername();
        }

        Long assignedTaskId = null;
        if (entity.getAssignedTask() != null) {
            assignedTaskId = entity.getAssignedTask().getId();
        }

        return new CommentDto(entity.getText(), entity.getCreatedAt(), creatorUsername, assignedTaskId);
    }

    @Override
    public Comment mapFrom(CommentDto dto) {
        if (dto == null) {
            return null;
        }

        Optional<Userx> foundUserOpt = this.userxService.findUserById(dto.creatorUsername());
        Userx userx = null;
        if (foundUserOpt.isPresent()) {
            userx = foundUserOpt.get();
        }

        Optional<Task> foundTaskOpt = this.taskService.findTaskById(dto.assignedTaskId());
        Task task = null;
        if (foundUserOpt.isPresent()) {
            task = foundTaskOpt.get();
        }

        return new Comment(dto.text(), dto.createdAt(), userx, task);
    }
}
