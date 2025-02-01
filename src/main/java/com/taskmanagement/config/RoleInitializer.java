package com.taskmanagement.config;

import com.taskmanagement.enums.RoleName;
import com.taskmanagement.model.Role;
import com.taskmanagement.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner loadRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role adminRole = Role.builder()
                        .name(RoleName.ADMIN)
                        .build();
                Role userRole = Role.builder()
                        .name(RoleName.USER)
                        .build();

                roleRepository.saveAll(List.of(adminRole, userRole));
            }
        };
    }
}
