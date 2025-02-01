package com.taskmanagement.service;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.TaskCreationRequest;
import com.taskmanagement.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task createTask(TaskCreationRequest taskCreationRequest);

    Task updateTask(TaskCreationRequest taskCreationRequest, Long id);

    Task getTask(Long id);

    void deleteTask(Long id);

    PaginatedResponse<Task> getAllTasks(Pageable pageable);
}
