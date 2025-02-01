package com.taskmanagement.service;


import com.taskmanagement.dto.AuthRequest;
import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.dto.DefaultResponse;
import com.taskmanagement.dto.PinRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    DefaultResponse createPin(PinRequest pinCreationRequest);

    DefaultResponse validatePin(PinRequest validatePinRequest);
}
