package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Comment;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.CommentRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserxService userxService;

    private final TaskService taskService;

    public CommentService(CommentRepository commentRepository,
                          UserxService userxService, TaskService taskService) {
        this.commentRepository = commentRepository;
        this.userxService = userxService;
        this.taskService = taskService;
    }

    public Optional<Comment> findCommentById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return commentRepository.findById(id);
    }

    public List<Comment> findAllByCreatorUsername(String username) {
        Optional<Userx> foundUserOpt = userxService.findUserById(username);
        if (foundUserOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Userx userx = foundUserOpt.get();
        return commentRepository.findAllByCreator(userx);
     }

     public List<Comment> findAllByTaskId(Long id) {
        Optional<Task> foundTaskOpt = taskService.findTaskById(id);
        if (foundTaskOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Task task = foundTaskOpt.get();
        return commentRepository.findAllByAssignedTask(task);
     }
}
