package dev.ddanny165.taskManagement.rest.dto;

import java.util.List;

public record ProjectDto(String name, List<String> assignedEmployees, List<Long> assignedTasks) { }
