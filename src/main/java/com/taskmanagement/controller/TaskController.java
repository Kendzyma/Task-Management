package com.taskmanagement.controller;


import com.taskmanagement.dto.ApiResponse;
import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.TaskCreationRequest;
import com.taskmanagement.model.Task;
import com.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("")
    public ApiResponse<Task> createTask( @Valid @RequestBody TaskCreationRequest taskCreationRequest) {
       return ApiResponse.ok(taskService.createTask(taskCreationRequest));
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> getTask(@PathVariable("id") Long id) {
        return ApiResponse.ok(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApiResponse<Task> updateTask(@RequestBody @Valid TaskCreationRequest taskCreationRequest, @PathVariable("id") Long id) {
        return ApiResponse.ok(taskService.updateTask(taskCreationRequest,id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApiResponse<String> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ApiResponse.ok("Task deleted");
    }

    @GetMapping("")
    public ApiResponse<PaginatedResponse<Task>> getAllTasks(Pageable pageable) {
        return ApiResponse.ok(taskService.getAllTasks(pageable));
    }

}
