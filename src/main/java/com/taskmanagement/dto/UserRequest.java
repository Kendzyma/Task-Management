package com.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taskmanagement.annotation.ValidPassword;
import com.taskmanagement.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Set;

public record UserRequest(
        @NotNull(message = "first name is required")
        String firstName,
        @NotNull(message = "last name is required")
        String lastName,
        @NotNull(message = "email is required")
        @Email(message = "invalid email format")
        String email,
        @NotNull(message = "phone number is required")
        @Pattern(
                regexp = "^\\+?[0-9]{11}$",
                message = "invalid phone number format, must be exactly 11 digits"
        )
        String phoneNumber,
        @NotNull(message = "password is required")
        @ValidPassword(message = "Password must contain letters, a number, and a special symbol")
        String password,

        @NotNull(message = "date of birth is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate dateOfBirth,
        @NotEmpty(message = "roles is required")
        Set<RoleName> roles
) {
}
