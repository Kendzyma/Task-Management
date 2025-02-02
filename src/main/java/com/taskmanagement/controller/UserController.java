package com.taskmanagement.controller;

import com.taskmanagement.dto.ApiResponse;
import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.UserDto;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.model.User;
import com.taskmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Create a new user")
    @PostMapping()
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.create(userRequest));
    }

    @Operation(
            summary = "Update a user",
            description = "Update a user")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserRequest userRequest, @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.update(userRequest,id));
    }

    @Operation(
            summary = "Get a user",
            description = "Get a user")
    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(
            summary = "Get all users",
            description = "Get all users")
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete a user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApiResponse<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok("User deleted");
    }

    @Operation(
            summary = "Search user by name",
            description = "Search user by name")
    @GetMapping("/search")
    public ApiResponse<PaginatedResponse<User>> getUserTasks(@RequestParam("name") String name,
                                                             Pageable pageable) {
        return ApiResponse.ok(userService.searchUser(name,pageable));
    }
}
