package dev.ddanny165.taskManagement.rest.dto;

import java.time.LocalDateTime;

public record CommentDto(String text, LocalDateTime createdAt, String creatorUsername, Long assignedTaskId) { }
