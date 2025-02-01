package com.taskmanagement.service;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.UserDto;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto create(UserRequest userRequest);

    UserDto update(UserRequest userRequest, Long id);

    User getUser(Long id);

    List<UserDto> getAllUsers(Pageable pageable);

    void deleteUser(Long id);

    PaginatedResponse<User> searchUser(String name, Pageable pageable);
}
