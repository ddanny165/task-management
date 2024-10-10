package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.ProjectDto;
import dev.ddanny165.taskManagement.services.TaskService;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope("application")
public class ProjectMapper implements DTOMapper<Project, ProjectDto> {

    private final UserxService userxService;
    private final TaskService taskService;

    public ProjectMapper(UserxService userxService, TaskService taskService) {
        this.userxService = userxService;
        this.taskService = taskService;
    }

    @Override
    public ProjectDto mapTo(Project entity) {
        List<String> assignedEmployees = entity.getAssignedEmployees()
                .stream()
                .map(Userx::getUsername)
                .toList();

        List<Long> assignedTasks = entity.getAssignedTasks()
                .stream()
                .map(Task::getId)
                .toList();

        return new ProjectDto(entity.getName(), assignedEmployees, assignedTasks);
    }

    @Override
    public Project mapFrom(ProjectDto dto) {
        Project projectEntity = new Project(dto.name());

        if (dto.assignedEmployees() != null) {
            projectEntity.setAssignedEmployees(
                    dto.assignedEmployees().stream()
                            .map(un -> {
                                Optional<Userx> foundUserOpt = this.userxService.findUserById(un);
                                return foundUserOpt.orElse(null);
                            })
                            .filter(user -> user != null)
                            .toList()
            );
        }

        if (dto.assignedTasks() != null) {
            projectEntity.setAssignedTasks(
                    dto.assignedTasks().stream()
                            .map(tid -> {
                                Optional<Task> foundTaskOpt = this.taskService.findTaskById(tid);
                                return foundTaskOpt.orElse(null);
                            }).filter(task -> task != null)
                            .toList()
            );
        }

        return projectEntity;
    }
}
