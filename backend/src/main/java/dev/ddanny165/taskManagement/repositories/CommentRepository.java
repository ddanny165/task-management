package dev.ddanny165.taskManagement.repositories;

import dev.ddanny165.taskManagement.models.Comment;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;

import java.util.List;

/**
 * Repository for managing {@link Comment} entities.
 */
public interface CommentRepository extends AbstractRepository<Comment, Long> {
    List<Comment> findAllByCreator(Userx userx);
    List<Comment> findAllByAssignedTask(Task task);
}
