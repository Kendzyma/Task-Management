package com.taskmanagement.repository;

import com.taskmanagement.enums.RoleName;
import com.taskmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
