package com.taskmanagement.controller;
import com.taskmanagement.dto.*;
import com.taskmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Login",
            description = "Login to the system")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ApiResponse.ok(authService.login(authRequest));
    }


    @PostMapping("/create-pin")
    public ApiResponse<String> createPin(@Valid @RequestBody PinRequest pinCreationRequest) {
         authService.createPin(pinCreationRequest);
        return ApiResponse.ok("Pin created");
    }

    @PostMapping("/validate-pin")
    public ApiResponse<String> validatePin(@Valid @RequestBody PinRequest validatePinRequest) {
        authService.validatePin(validatePinRequest);
        return ApiResponse.ok("Pin validated");
    }
}
