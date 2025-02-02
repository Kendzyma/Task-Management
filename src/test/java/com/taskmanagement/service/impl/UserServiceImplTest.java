package com.taskmanagement.service.impl;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.UserDto;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.enums.RoleName;
import com.taskmanagement.exception.BadRequestException;
import com.taskmanagement.model.Identity;
import com.taskmanagement.model.Role;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.IdentityRepository;
import com.taskmanagement.repository.RoleRepository;
import com.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private IdentityRepository identityRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private Role role;
    private Identity identity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        role = new Role(1L, RoleName.USER);

        userRequest = new UserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "+12345678901",
                "Secure@123",
                LocalDate.of(1990, 1, 1),
                Set.of(RoleName.USER)
        );

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+12345678901")
                .roles(Set.of(role))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        identity = new Identity();
        identity.setUser(user);
        identity.setPassword("encodedPassword");

        userDto = new UserDto(1L, "john.doe@example.com", "+12345678901","John" , "Doe");
    }

    @Test
    void createUser_ShouldCreateNewUser_WhenValidRequest() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(modelMapper.map(any(), eq(UserDto.class))).thenReturn(userDto);

        UserDto createdUser = userService.create(userRequest);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        verify(identityRepository, times(1)).save(any(Identity.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.create(userRequest));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);
        when(modelMapper.map(any(), eq(UserDto.class))).thenReturn(userDto);

        UserDto updatedUser = userService.update(userRequest, 1L);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void getUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.getUser(1L));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void searchUser_ShouldReturnPaginatedResponse() {
        Page<User> usersPage = new PageImpl<>(List.of(user));
        when(userRepository.searchByName(anyString(), any())).thenReturn(usersPage);

        PaginatedResponse<User> result = userService.searchUser("John", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
