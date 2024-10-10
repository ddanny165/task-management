package dev.ddanny165.taskManagement.rest.controllers;

import dev.ddanny165.taskManagement.models.Comment;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.rest.dto.CommentDto;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorType;
import dev.ddanny165.taskManagement.rest.mappers.CommentMapper;
import dev.ddanny165.taskManagement.rest.mappers.TaskMapper;
import dev.ddanny165.taskManagement.services.CommentService;
import dev.ddanny165.taskManagement.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskRESTController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    public TaskRESTController(TaskService taskService, TaskMapper taskMapper,
                              CommentService commentService, CommentMapper commentMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<Task> foundTasks = taskService.findAllTasks();
        List<TaskDto> taskDataToReturn = foundTasks.stream()
                .map(taskMapper::mapTo)
                .toList();

        return ResponseEntity.ok(taskDataToReturn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskData(@PathVariable Long id) {
        Optional<Task> taskOpt = taskService.findTaskById(id);
        if (taskOpt.isPresent()) {
            return new ResponseEntity<>(taskMapper.mapTo(taskOpt.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A task with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsAssociatedWithTask(@PathVariable Long id) {
        List<Comment> foundComments = commentService.findAllByTaskId(id);
        return ResponseEntity.ok(foundComments.stream()
                .map(commentMapper::mapTo)
                .toList());
    }

    @PostMapping("")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        if (!taskService.isAValidStatus(taskDto.status())) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_TASK_STATUS_NAME,
                    "The task status of the task to create is invalid."), HttpStatus.BAD_REQUEST);
        }

        if (!taskService.isAValidPriority(taskDto.priority())) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_TASK_PRIORITY_NAME,
                    "The priority of the task to create is invalid."), HttpStatus.BAD_REQUEST);
        }

        if (taskDto.title() == null || taskDto.createdAt() == null || taskDto.creatorUsername() == null) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_NULL_PARAMETER,
                    "At least one of {title, createdAt, creatorUsername} parameters is equal to null!"),
                    HttpStatus.BAD_REQUEST);
        }

        Task savedTask = taskService.saveTask(taskMapper.mapFrom(taskDto));
        return new ResponseEntity<>(taskMapper.mapTo(savedTask), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Optional<Task> updatedTaskOpt = this.taskService.updateTask(id, taskMapper.mapFrom(taskDto));

        if (updatedTaskOpt.isEmpty()) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A task with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(taskMapper.mapTo(updatedTaskOpt.get()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        this.taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
     }
}
