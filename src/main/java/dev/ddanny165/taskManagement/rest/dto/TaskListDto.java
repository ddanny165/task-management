package dev.ddanny165.taskManagement.rest.dto;

import java.util.List;

public record TaskListDto(String name, String visibility, String creatorUsername, List<Long> assignedTaskIds) { }
