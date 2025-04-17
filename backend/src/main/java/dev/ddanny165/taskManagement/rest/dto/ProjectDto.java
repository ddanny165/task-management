package dev.ddanny165.taskManagement.rest.dto;

import java.util.List;

public record ProjectDto(Long id, String name, List<String> assignedEmployees, List<Long> assignedTasks) { }
