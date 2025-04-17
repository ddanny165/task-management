package dev.ddanny165.taskManagement.rest.controllers;

import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.ProjectDto;
import dev.ddanny165.taskManagement.rest.dto.TaskDto;
import dev.ddanny165.taskManagement.rest.dto.UserxDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorType;
import dev.ddanny165.taskManagement.rest.mappers.ProjectMapper;
import dev.ddanny165.taskManagement.rest.mappers.TaskMapper;
import dev.ddanny165.taskManagement.rest.mappers.UserxMapper;
import dev.ddanny165.taskManagement.services.ProjectService;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectRESTController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final UserxService userxService;
    private final UserxMapper userxMapper;

    public ProjectRESTController(ProjectService projectService, ProjectMapper projectMapper,
                                 TaskService taskService, TaskMapper taskMapper,
                                 UserxService userxService, UserxMapper userxMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.userxService = userxService;
        this.userxMapper = userxMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<Project> foundProjects = projectService.findAllProjects();

        return ResponseEntity.ok(foundProjects.stream()
                .map(projectMapper::mapTo)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectData(@PathVariable Long id) {
        Optional<Project> projectOpt = projectService.findProjectById(id);
        if (projectOpt.isPresent()) {
            return new ResponseEntity<>(projectMapper.mapTo(projectOpt.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A project with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getAssignedTasks(@PathVariable Long id) {
        List<Task> foundTasks = taskService.findAllTasksByProjectId(id);
        List<TaskDto> taskDataToReturn = foundTasks.stream()
                .map(taskMapper::mapTo)
                .toList();

        return ResponseEntity.ok(taskDataToReturn);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserxDto>> getAssignedUsers(@PathVariable Long id) {
        List<Userx> foundUsers = userxService.findAllUsersByProjectId(id);
        List<UserxDto> userDataToReturn = foundUsers.stream()
                .map(userxMapper::mapTo)
                .toList();

        return ResponseEntity.ok(userDataToReturn);
    }

    @PostMapping("")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        if (projectDto.name() == null || projectDto.name().length() == 0) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_PROJECT_NAME,
                    "The project name of a new project can not be empty or equal to null!"),
                    HttpStatus.BAD_REQUEST);
        }

        Project savedProject = this.projectService.saveProject(projectMapper.mapFrom(projectDto));
        return new ResponseEntity<>(projectMapper.mapTo(savedProject), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDto dto) {
        Optional<Project> updatedProjectOpt = this.projectService.updateProject(id, projectMapper.mapFrom(dto));

        if (updatedProjectOpt.isEmpty()) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A project with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(projectMapper.mapTo(updatedProjectOpt.get()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        this.projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
