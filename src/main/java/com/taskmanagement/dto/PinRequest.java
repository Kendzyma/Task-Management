package com.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PinRequest(
        @NotBlank(message = "PIN is required")
        @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
        @Pattern(regexp = "\\d{4}", message = "PIN must contain only numbers")
         String pin,
        @NotNull(message = "User ID is required")
        Long userId
){}
