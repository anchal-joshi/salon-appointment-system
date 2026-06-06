package com.salon.appointment.controller;

import com.salon.appointment.dto.request.StaffLeaveRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.StaffLeaveResponse;
import com.salon.appointment.dto.response.StaffScheduleResponse;
import com.salon.appointment.entity.User;
import com.salon.appointment.service.StaffScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@PreAuthorize("hasRole('STAFF')")
public class StaffScheduleController {
    private final StaffScheduleService staffScheduleService;

    public StaffScheduleController(StaffScheduleService staffScheduleService) {
        this.staffScheduleService = staffScheduleService;
    }

    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<StaffScheduleResponse>>>getMySchedule(
            @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(ApiResponse.success("Schedule fetched", staffScheduleService.getMySchedule(currentUser)));
    }

    @GetMapping("/leaves")
    public ResponseEntity<ApiResponse<List<StaffLeaveResponse>>>getMyleaves(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Leaves fetched", staffScheduleService.getMyLeaves(currentUser)));
    }

    @PostMapping("/leaves")
    public ResponseEntity<ApiResponse<StaffLeaveResponse>>applyLeave(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody StaffLeaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Leave applied", staffScheduleService.applyLeave(currentUser, request)));
    }

    @DeleteMapping("/leaves/{leaveId}")
    public ResponseEntity<ApiResponse<Void>>cancelLeave(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long leaveId
    ){
        staffScheduleService.cancelLeave(currentUser, leaveId);
        return ResponseEntity.ok(ApiResponse.success("Leave cancelled"));
    }
}
