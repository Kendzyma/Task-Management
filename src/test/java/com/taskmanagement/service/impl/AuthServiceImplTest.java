
package com.taskmanagement.service.impl;

import com.taskmanagement.dto.AuthRequest;
import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.dto.DefaultResponse;
import com.taskmanagement.dto.PinRequest;
import com.taskmanagement.enums.IdentityStatus;
import com.taskmanagement.exception.AuthenticationException;
import com.taskmanagement.model.Identity;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.IdentityRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private IdentityRepository identityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private Identity identity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        identity = new Identity();
        identity.setUser(user);
        identity.setPasswordFailureCount(0);
        identity.setPasswordStatus(IdentityStatus.ACTIVE);
        identity.setPinStatus(IdentityStatus.ACTIVE);
        identity.setPin("1234");
    }

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(any())).thenReturn("mock-token");

        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals("mock-token", response.token());
    }

    @Test
    void testLogin_Failed_WrongPassword() {
        AuthRequest request = new AuthRequest("test@example.com", "wrong-password");

        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid Credentials"));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.login(request));
        assertEquals("wrong email or password", exception.getMessage());
    }

    @Test
    void testCreatePin_Success() {
        PinRequest pinRequest = new PinRequest("1234", 1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));
        when(passwordEncoder.encode(any())).thenReturn("encoded-pin");

        DefaultResponse response = authService.createPin(pinRequest);
        assertNotNull(response);
    }

    @Test
    void testCreatePin_NewIdentity() {
        PinRequest pinRequest = new PinRequest("1234", 1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded-pin");

        DefaultResponse response = authService.createPin(pinRequest);
        assertNotNull(response);
    }

    @Test
    void testValidatePin_Success() {
        PinRequest pinRequest = new PinRequest("1234", 1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        DefaultResponse response = authService.validatePin(pinRequest);
        assertNotNull(response);
    }

    @Test
    void testValidatePin_InvalidPin() {
        PinRequest pinRequest = new PinRequest("0000", 1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.validatePin(pinRequest));
        assertEquals("Invalid PIN", exception.getMessage());
    }

    @Test
    void testValidatePin_Blocked() {
        PinRequest pinRequest = new PinRequest("1234", 1L);
        identity.setPinStatus(IdentityStatus.BLOCKED);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(identityRepository.findByUser(any())).thenReturn(Optional.of(identity));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.validatePin(pinRequest));
        assertEquals("PIN is blocked", exception.getMessage());
    }
}
