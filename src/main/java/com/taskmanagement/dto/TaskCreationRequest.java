package com.taskmanagement.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.taskmanagement.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record TaskCreationRequest(
        String title,
        String description,
        @NotNull(message = "due date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate dueDate,
        Long userId,
        TaskStatus status
) {
}
