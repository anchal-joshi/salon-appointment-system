package com.salon.appointment.controller;

import com.salon.appointment.dto.request.LoginRequest;
import com.salon.appointment.dto.request.RefreshTokenRequest;
import com.salon.appointment.dto.request.RegisterRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.AuthResponse;
import com.salon.appointment.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>>register(
            @Valid @RequestBody RegisterRequest request
            ){
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>login(
            @Valid @RequestBody LoginRequest request
            ){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>>refresh(
            @Valid @RequestBody RefreshTokenRequest request
            ){
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>>logout(
            @RequestHeader ("Authorization") String authHeader
    ){
        authService.logout(authHeader);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}
