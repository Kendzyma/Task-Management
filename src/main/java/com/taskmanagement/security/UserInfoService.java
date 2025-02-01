package com.taskmanagement.security;

import com.taskmanagement.exception.AuthenticationException;
import com.taskmanagement.model.Identity;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.IdentityRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoService implements UserDetailsService {
    private final UserRepository repository;
    private final IdentityRepository identityRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetail = repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new AuthenticationException("User not found: " + username));

        Identity identity = identityRepository.findByUser(userDetail)
                .orElseThrow(() -> new AuthenticationException("Identity not found for user"));

        UserInfoDetails userInfoDetails = new UserInfoDetails(userDetail, identity);
        log.info("authorities {}", userInfoDetails.getAuthorities());
        return userInfoDetails;
    }
}
