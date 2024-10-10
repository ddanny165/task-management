package dev.ddanny165.taskManagement.services;

import dev.ddanny165.taskManagement.models.Project;
import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.repositories.ProjectRepository;
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

    public ProjectService(ProjectRepository projectRepository, UserxService userxService) {
        this.projectRepository = projectRepository;
        this.userxService = userxService;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
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
}
