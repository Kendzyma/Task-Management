package com.taskmanagement.repository;

import com.taskmanagement.model.Identity;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Optional<Identity> findByUser(User user);
}
