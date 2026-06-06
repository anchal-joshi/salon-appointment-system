package com.salon.appointment.controller;

import com.salon.appointment.dto.request.StaffScheduleRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.StaffLeaveResponse;
import com.salon.appointment.dto.response.StaffScheduleResponse;
import com.salon.appointment.service.StaffScheduleService;
import jakarta.validation.Valid;
import org.hibernate.boot.internal.Abstract;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/staff")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStaffController {

    private final StaffScheduleService staffScheduleService;

    public AdminStaffController(StaffScheduleService staffScheduleService) {
        this.staffScheduleService = staffScheduleService;
    }

    @PostMapping("/{staffId}/schedule")
    public ResponseEntity<ApiResponse<StaffScheduleResponse>>setSchedule(
            @PathVariable Long staffId,
            @Valid @RequestBody StaffScheduleRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success("Schedule set", staffScheduleService.setSchedule(staffId, request)));
    }

    @GetMapping("/{staffId}/schedule")
    public ResponseEntity<ApiResponse<List<StaffScheduleResponse>>>getStaffSchedule(
            @PathVariable Long staffId
    ){
        return ResponseEntity.ok(ApiResponse.success("Schedule fetched", staffScheduleService.getStaffSchedule(staffId)));
    }

    @GetMapping("/leaves")
    public ResponseEntity<ApiResponse<List<StaffLeaveResponse>>>getLeavesByDate(
            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)LocalDate date
            ){
        return ResponseEntity.ok(ApiResponse.success("Leaves fetched", staffScheduleService.getLeavesByDate(date)));
    }

    @GetMapping("/{staffId}/leaves")
    public ResponseEntity<ApiResponse<List<StaffLeaveResponse>>>getAllLeavesOfStaff(
            @PathVariable Long staffId
    ){
        return ResponseEntity.ok(ApiResponse.success("Leaves fetched", staffScheduleService.getAllLeavesOfStaff(staffId)));
    }

    @DeleteMapping("/leaves/{leaveId}")
    public ResponseEntity<ApiResponse<Void>>deleteLeave(@PathVariable Long leaveId){
        staffScheduleService.deleteLeave(leaveId);
        return ResponseEntity.ok(ApiResponse.success("Leave deleted"));
    }
}
