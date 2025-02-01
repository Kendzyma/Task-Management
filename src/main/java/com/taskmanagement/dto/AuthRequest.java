package com.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;

public record AuthRequest(
    @NotNull(message = "Username is required")
     String email,
    @NotNull(message = "Password is required")
     String password){
}
