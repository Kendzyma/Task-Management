package com.taskmanagement.service.impl;

import com.taskmanagement.dto.PaginatedResponse;
import com.taskmanagement.dto.UserDto;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.enums.IdentityStatus;
import com.taskmanagement.exception.BadRequestException;
import com.taskmanagement.model.Identity;
import com.taskmanagement.model.Role;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.IdentityRepository;
import com.taskmanagement.repository.RoleRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final IdentityRepository identityRepository;

    @Override
    public UserDto create(UserRequest userRequest) {
        log.info("Creating user : {}", userRequest);

        userRepository.findByEmailIgnoreCase(userRequest.email())
                .ifPresent(email -> {
                    throw new BadRequestException(
                            String.format("User with email %s already exists", userRequest.email()));
                });

        Set<Role> roles = userRequest.roles().stream().map(role -> roleRepository.findByName(role)
                        .orElseThrow(() ->
                                new BadRequestException(String.format("Role %s does not exist", role.name()))))
                .collect(Collectors.toSet());

        User user = User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .dateOfBirth(userRequest.dateOfBirth())
                .phoneNumber(userRequest.phoneNumber())
                .roles(roles)
                .build();
        User savedUser = userRepository.save(user);

        Identity identity = new Identity();
        identity.setUser(savedUser);
        identity.setPassword(passwordEncoder.encode(userRequest.password()));
        identity.setPasswordStatus(IdentityStatus.ACTIVE);
        identityRepository.save(identity);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto update(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Set<Role> roles = userRequest.roles().stream().map(role -> roleRepository.findByName(role)
                        .orElseThrow(() ->
                                new BadRequestException(String.format("Role %s does not exist", role.name()))))
                .collect(Collectors.toSet());

        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setRoles(roles);
        user.setDateOfBirth(userRequest.dateOfBirth());

        log.info("Saving user: {}", user);
        User updateUser = userRepository.save(user);

        return modelMapper.map(updateUser, UserDto.class);
    }

    @Override
    public User getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return user;
    }

    @Override
    public List<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        log.info("Deleting user with id: {}", id);
        userRepository.delete(user);
    }

    @Override
    public PaginatedResponse<User> searchUser(String name, Pageable pageable) {
        Page<User> users = userRepository.searchByName(name, pageable);
        log.info("Users found: {}", users);
        return new PaginatedResponse<>(users);
    }
}
