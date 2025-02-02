package com.taskmanagement.controller;


import com.taskmanagement.dto.ApiResponse;
import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.TaskCreationRequest;
import com.taskmanagement.model.Task;
import com.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Create Task",
            description = "Create Task")
    @PostMapping("")
    public ApiResponse<Task> createTask( @Valid @RequestBody TaskCreationRequest taskCreationRequest) {
        return ApiResponse.ok(taskService.createTask(taskCreationRequest));
    }

    @Operation(
            summary = "Get Task",
            description = "Get Task")
    @GetMapping("/{id}")
    public ApiResponse<Task> getTask(@PathVariable("id") Long id) {
        return ApiResponse.ok(taskService.getTask(id));
    }

    @Operation(
            summary = "Update Task",
            description = "Update Task")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApiResponse<Task> updateTask(@RequestBody @Valid TaskCreationRequest taskCreationRequest, @PathVariable("id") Long id) {
        return ApiResponse.ok(taskService.updateTask(taskCreationRequest,id));
    }


    @Operation(
            summary = "Delete Task",
            description = "Delete Task")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApiResponse<String> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ApiResponse.ok("Task deleted");
    }

    @Operation(
            summary = "Get All Tasks",
            description = "Get All Tasks")

    @GetMapping("")
    public ApiResponse<PaginatedResponse<Task>> getAllTasks(Pageable pageable) {
        return ApiResponse.ok(taskService.getAllTasks(pageable));
    }

}
