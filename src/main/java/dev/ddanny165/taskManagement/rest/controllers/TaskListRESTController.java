package dev.ddanny165.taskManagement.rest.controllers;

import dev.ddanny165.taskManagement.models.TaskList;
import dev.ddanny165.taskManagement.rest.dto.TaskListDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorType;
import dev.ddanny165.taskManagement.rest.mappers.TaskListMapper;
import dev.ddanny165.taskManagement.services.TaskListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasklists")
public class TaskListRESTController {
    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListRESTController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<TaskListDto>> getAllTaskLists() {
        List<TaskList> foundTaskLists = taskListService.findAllTaskLists();
        List<TaskListDto> taskListDtos = foundTaskLists.stream()
                .map(taskListMapper::mapTo)
                .toList();

        return ResponseEntity.ok(taskListDtos);
    }

    @GetMapping("public")
    public ResponseEntity<List<TaskListDto>> getAllPublicTaskLists() {
        List<TaskList> foundPublicTaskLists = taskListService.findAllPublicTaskLists();
        List<TaskListDto> taskListDtos = foundPublicTaskLists.stream()
                .map(taskListMapper::mapTo)
                .toList();

        return ResponseEntity.ok(taskListDtos);
    }

    @PostMapping("")
    public ResponseEntity<?> createTaskList(@RequestBody TaskListDto taskListDto) {
        if (!taskListService.isAValidVisibility(taskListDto.visibility())) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_TASK_LIST_VISIBILITY,
                    "The task list visibility of the task list to create is invalid."), HttpStatus.BAD_REQUEST);
        }

        TaskList savedTaskList = taskListService.saveTaskList(taskListMapper.mapFrom(taskListDto));
        return new ResponseEntity<>(taskListMapper.mapTo(savedTaskList), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTaskListData(@PathVariable Long id) {
        Optional<TaskList> taskListOpt = taskListService.findTaskListById(id);
        if (taskListOpt.isPresent()) {
            return new ResponseEntity<>(taskListMapper.mapTo(taskListOpt.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A task list with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTaskList(@PathVariable Long id, @RequestBody TaskListDto taskListDto) {
        Optional<TaskList> updatedTaskListOpt = this.taskListService.updateTaskList(id, taskListMapper.mapFrom(taskListDto));

        if (updatedTaskListOpt.isEmpty()) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.NOT_FOUND,
                    "A task list with the given id: |" + id + "| is not found"),
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(taskListMapper.mapTo(updatedTaskListOpt.get()), HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTaskList(@PathVariable Long id) {
        this.taskListService.deleteTaskList(id);
        return ResponseEntity.noContent().build();
    }
}
