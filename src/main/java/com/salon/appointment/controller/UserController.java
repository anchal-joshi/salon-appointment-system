package com.salon.appointment.controller;

import com.salon.appointment.dto.request.ChangePasswordRequest;
import com.salon.appointment.dto.request.UpdateProfileRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.UserResponse;
import com.salon.appointment.entity.User;
import com.salon.appointment.service.UserService;
import jakarta.validation.Valid;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>>getMyProfile(
            @AuthenticationPrincipal User currentUser
            ){
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", userService.getMyProfile(currentUser)));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>>updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateProfileRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success("Profile updated", userService.updateMyProfile(currentUser, request)));
    }

    @PatchMapping("/me/change-password")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>>changePassword(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ChangePasswordRequest request
            ){
        userService.changePassword(currentUser, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}
