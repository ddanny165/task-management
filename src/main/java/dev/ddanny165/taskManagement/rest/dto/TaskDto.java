package dev.ddanny165.taskManagement.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDto(Long id, String title, String description, String status, String priority,
                      LocalDateTime createdAt, LocalDateTime toBeDoneUntil, String assignedEmployeeUsername,
                      String creatorUsername, Long assignedProjectId, List<String> assignedTagIds,
                      List<Long> assignedCommentIds) { }
