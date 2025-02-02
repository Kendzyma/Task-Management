package com.taskmanagement.service.impl;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.TaskCreationRequest;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;
    private TaskCreationRequest taskRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Task description")
                .dueDate(LocalDate.now())
                .status(TaskStatus.TODO)
                .user(user)
                .build();

        taskRequest = new TaskCreationRequest("Test Task", "Task description", LocalDate.now(), 1L, TaskStatus.TODO);
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(taskRequest);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.createTask(taskRequest));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(taskRequest, 1L);

        assertNotNull(updatedTask);
        assertEquals("Test Task", updatedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(taskRequest, 1L));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void getTask_ShouldReturnTask_WhenFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTask(1L);

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
    }

    @Test
    void getTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.getTask(1L));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllTasks_ShouldReturnPaginatedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        PaginatedResponse<Task> response = taskService.getAllTasks(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
    }
}