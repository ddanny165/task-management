package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Task;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.ProjectRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserxService userxService;

    public ProjectService(ProjectRepository projectRepository, @Lazy UserxService userxService) {
        this.projectRepository = projectRepository;
        this.userxService = userxService;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return projectRepository.findById(id);
    }

    public List<Project> findAllProjectsByUserxUsername(String username) {
        Optional<Userx> foundUserxOpt = userxService.findUserById(username);
        if (foundUserxOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Userx foundUserx = foundUserxOpt.get();
        return foundUserx.getAssignedProjects();
    }

    public Project saveProject(Project project) {
        if (project == null) {
            return null;
        }

        return projectRepository.save(project);
    }

    public Optional<Project> updateProject(Long id, Project updateData) {
        if (updateData == null || id == null) {
            return Optional.empty();
        }

        Optional<Project> foundProjectOpt = projectRepository.findById(id);
        if (foundProjectOpt.isEmpty()) {
            return Optional.empty();
        }

        Project foundProject = foundProjectOpt.get();

        if (updateData.getName() != null) {
            foundProject.setName(updateData.getName());
        }

        if (updateData.getAssignedEmployees() != null) {
            List<Userx> assignedEmployees = new ArrayList<>();
            updateData.getAssignedEmployees().forEach(ae -> assignedEmployees.add(ae));
            foundProject.setAssignedEmployees(assignedEmployees);
        }

        if (updateData.getAssignedTasks() != null) {
            List<Task> assignedTasks = new ArrayList<>();
            updateData.getAssignedTasks().forEach(at -> {
                at.setAssignedProject(foundProject);
                assignedTasks.add(at);
            });
            foundProject.setAssignedTasks(assignedTasks);
        }

        Project updatedProject = projectRepository.save(foundProject);

        return Optional.of(updatedProject);
    }

    public void deleteProject(Long id) {
        if (id != null) {
            Optional<Project> foundProject = this.projectRepository.findById(id);
            foundProject.ifPresent(p -> {

                p.getAssignedTasks().forEach(at -> at.setAssignedProject(null));
                p.getAssignedEmployees().forEach(ae -> {
                    List<Project> assignedProjects = ae.getAssignedProjects();
                    if (assignedProjects != null) {
                        assignedProjects.remove(p);
                    }
                });
                p.setAssignedTasks(null);
                p.setAssignedEmployees(null);
                this.projectRepository.delete(p);
            });
        }
    }
}
