package dev.ddanny165.taskManagement.rest.dto;

import java.time.LocalDateTime;

public record TaskDto(Long id, String title, String description, String status, String priority,
                      LocalDateTime toBeDoneUntil, String assignedEmployeeUsername) { }
