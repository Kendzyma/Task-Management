package com.taskmanagement.service.impl;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.TaskCreationRequest;
import com.taskmanagement.exception.BadRequestException;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    @Override
    public Task createTask(TaskCreationRequest taskCreationRequest) {
        User user = userRepository.findById(taskCreationRequest.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(taskCreationRequest.dueDate().isBefore(LocalDate.now())){
            throw new BadRequestException("Due date cannot be in the past");
        }
        Task task = Task.builder()
                .title(taskCreationRequest.title())
                .description(taskCreationRequest.description())
                .dueDate(taskCreationRequest.dueDate())
                .status(taskCreationRequest.status())
                .user(user)
                .build();

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskCreationRequest taskCreationRequest, Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(taskCreationRequest.title());
        task.setDescription(taskCreationRequest.description());
        task.setDueDate(taskCreationRequest.dueDate());
        task.setStatus(taskCreationRequest.status());

        return taskRepository.save(task);
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public PaginatedResponse<Task> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return new PaginatedResponse<>(tasks);
    }
}
