package com.taskmanagement.service.impl;

import com.taskmanagement.dto.AuthRequest;
import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.dto.DefaultResponse;
import com.taskmanagement.dto.PinRequest;
import com.taskmanagement.enums.IdentityStatus;
import com.taskmanagement.exception.AuthenticationException;
import com.taskmanagement.exception.BadRequestException;
import com.taskmanagement.model.Identity;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.IdentityRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.security.JwtService;
import com.taskmanagement.service.AuthService;
import com.taskmanagement.util.Contant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new AuthenticationException("User not found"));
        Identity identity = identityRepository.findByUser(user)
                .orElseThrow(() -> new AuthenticationException("Identity not found for user"));

        if (IdentityStatus.BLOCKED.equals(identity.getPasswordStatus())) {
            throw new AuthenticationException("Password is blocked");
        }

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        }catch (org.springframework.security.authentication.BadCredentialsException e){
            processLoginFailure(identity);
            throw new AuthenticationException("wrong email or password");
        }
        if (authentication.isAuthenticated()) {
            identity.setPasswordFailureCount(0);
            identityRepository.save(identity);
            String token = jwtService
                    .generateToken(request.email());
            return new AuthResponse(token);
        } else {
            processLoginFailure(identity);
            throw new AuthenticationException("wrong email or password");
        }
    }

    private void processLoginFailure(Identity identity) {
            int passwordFailureCount = identity.getPasswordFailureCount() + 1;

            if(passwordFailureCount >= Contant.MAX_PASSWORD_ATTEMPTS){
                identity.setPasswordStatus(IdentityStatus.BLOCKED);
                identity.setPasswordFailureCount(passwordFailureCount);
                identityRepository.save(identity);
            }

            else {
                identity.setPasswordFailureCount(passwordFailureCount);
                identityRepository.save(identity);
            }
    }

    @Override
    public DefaultResponse createPin(PinRequest pinCreationRequest) {
        User user = userRepository.findById(pinCreationRequest.userId())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        Optional<Identity> identity = identityRepository.findByUser(user);

        if(identity.isPresent()){
            identity.get().setPin(passwordEncoder.encode(pinCreationRequest.pin()));
            identity.get().setPinStatus(IdentityStatus.ACTIVE);
            identityRepository.save(identity.get());
        }
        else{
            Identity newIdentity = new Identity();
            newIdentity.setUser(user);
            newIdentity.setPin(passwordEncoder.encode(pinCreationRequest.pin()));
            newIdentity.setPinStatus(IdentityStatus.ACTIVE);
            identityRepository.save(newIdentity);
        }

        return new DefaultResponse();
    }

    @Override
    public DefaultResponse validatePin(PinRequest validatePinRequest) {
        User user = userRepository.findById(validatePinRequest.userId())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        Identity identity = identityRepository.findByUser(user)
                .orElseThrow(() -> new AuthenticationException("Identity not found for user"));

        if(IdentityStatus.BLOCKED.equals(identity.getPinStatus())){
            throw new AuthenticationException("PIN is blocked");
        }

        if(Boolean.FALSE.equals(passwordEncoder
                .matches(validatePinRequest.pin(), identity.getPin()))){
            processPinFailure(identity);
            identityRepository.save(identity);
            throw new AuthenticationException("Invalid PIN");
        }

        identity.setPinFailureCount(0);
        identityRepository.save(identity);

        return new DefaultResponse();
    }

    private void processPinFailure(Identity identity) {
        int pinFailureCount = identity.getPinFailureCount() + 1;
        if(pinFailureCount >= Contant.MAX_PIN_ATTEMPTS){
            identity.setPinStatus(IdentityStatus.BLOCKED);
            identity.setPinFailureCount(pinFailureCount);
        }
        else {
            identity.setPinFailureCount(pinFailureCount);
        }
    }
}
