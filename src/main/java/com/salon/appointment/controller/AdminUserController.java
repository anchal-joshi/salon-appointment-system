package com.salon.appointment.controller;

import com.salon.appointment.dto.request.CreateStaffRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.UserResponse;
import com.salon.appointment.entity.User;
import com.salon.appointment.enums.Role;
import com.salon.appointment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<UserResponse>>>getAllCustomers(){
        return ResponseEntity.ok(ApiResponse.success("Customers fetched",userService.getAllCustomers()));
    }

    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<List<UserResponse>>>getAllStaff(){
        return ResponseEntity.ok(ApiResponse.success("Staff fetched", userService.getAllStaff()));
    }

    @PostMapping("/staff")
    public ResponseEntity<ApiResponse<UserResponse>>createStaff(
            @Valid @RequestBody CreateStaffRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Staff created", userService.createStaff(request)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>>deactivateUser(@PathVariable Long id){
        userService.deactiveUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated"));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ApiResponse<Void>>reactivateUser(
            @PathVariable Long id
    ){
        userService.reactiveUser(id);
        return ResponseEntity.ok(ApiResponse.success("User reactivated"));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>>changeRole(
            @PathVariable Long id,
            @RequestParam Role newRole
            ){
        return ResponseEntity.ok(ApiResponse.success("Role updated", userService.changeUserRole(id, newRole)));
    }
}
