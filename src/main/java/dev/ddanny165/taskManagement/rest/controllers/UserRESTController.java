package dev.ddanny165.taskManagement.rest.controllers;

import dev.ddanny165.taskManagement.models.Comment;
import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.CommentDto;
import dev.ddanny165.taskManagement.rest.dto.ProjectDto;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.rest.dto.UserxDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorType;
import dev.ddanny165.taskManagement.rest.mappers.CommentMapper;
import dev.ddanny165.taskManagement.rest.mappers.ProjectMapper;
import dev.ddanny165.taskManagement.rest.mappers.TaskMapper;
import dev.ddanny165.taskManagement.rest.mappers.UserxMapper;
import dev.ddanny165.taskManagement.services.CommentService;
import dev.ddanny165.taskManagement.services.ProjectService;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    private final UserxService userxService;
    private final CommentService commentService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserxMapper userxMapper;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final CommentMapper commentMapper;

    public UserRESTController(UserxService userxService, UserxMapper userxMapper,
                               TaskService taskService, TaskMapper taskMapper,
                              CommentService commentService, CommentMapper commentMapper,
                              ProjectService projectService, ProjectMapper projectMapper) {
        this.userxService = userxService;
        this.userxMapper = userxMapper;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<UserxDto>> getAllUsers() {
        List<Userx> foundUsers = userxService.findAllUsers();
        List<UserxDto> userDataToReturn = foundUsers.stream()
                .map(userxMapper::mapTo)
                .toList();

        return ResponseEntity.ok(userDataToReturn);
    }

    @GetMapping("/{username}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsAssociatedWithUser(@PathVariable String username) {
        List<Comment> foundComments = commentService.findAllByCreatorUsername(username);
        List<CommentDto> commentDataToReturn = foundComments.stream()
                .map(commentMapper::mapTo)
                .toList();

        return ResponseEntity.ok(commentDataToReturn);
    }

    @GetMapping("{username}/tasks")
    public ResponseEntity<List<TaskDto>> getAssignedTasks(@PathVariable String username) {
        List<Task> foundTasks = taskService.findAllTasksByEmployeeId(username);
        List<TaskDto> taskDataToReturn = foundTasks.stream()
                .map(taskMapper::mapTo)
                .toList();

        return ResponseEntity.ok(taskDataToReturn);
    }

    @GetMapping("{username}/projects")
    public ResponseEntity<List<ProjectDto>> getAssignedProjects(@PathVariable String username) {
        List<Project> foundTasks = projectService.findAllProjectsByUserxUsername(username);
        List<ProjectDto> projectDataToReturn = foundTasks.stream()
                .map(projectMapper::mapTo)
                .toList();

        return ResponseEntity.ok(projectDataToReturn);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserxDto userxDto) {
        if (userxDto.username() == null || userxDto.username().equals("")) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.USERNAME_EQUALS_TO_NULL,
                    "The username of a new user can not be equal to null or empty!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userxDto.firstName() == null || userxDto.lastName() == null
                || userxDto.email() == null || userxDto.password() == null) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_NULL_PARAMETER,
                    "At least one of the provided parameters is equal to null!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userxService.doesAlreadyExistWithSuchUsername(userxDto.username())) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.USERNAME_ALREADY_EXISTS,
                    "The user with a provided username already exists in our system!"),
                    HttpStatus.BAD_REQUEST);
        }

        Userx userx = userxService.saveUser(userxMapper.mapFrom(userxDto));
        return new ResponseEntity<>(userxMapper.mapTo(userx), HttpStatus.CREATED);
    }

    // TODO: login user
    // POST endpoint "/auth/login"
    // Request Body: username + password
    // Response: JWT token

    // TODO: update user
    // Update user information such as email/password/firstName/LastName
    // method parameters: @RequestBody Userx userxDto, Authentication authentication
    // Request Body: UserxDto
    // Authentication via JWT token

    // TODO: get authenticated user information
    // method parameters: Authentication authentication
    // Authentication via JWT token (should be included in the request header)
}
