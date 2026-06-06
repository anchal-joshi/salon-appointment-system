package com.salon.appointment.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffLeaveResponse {
    private Long id;
    private Long staffId;
    private String staffName;
    private LocalDate leaveDate;
    private String reason;
    private LocalDateTime createdAt;


    public StaffLeaveResponse() {
    }

    public StaffLeaveResponse(Long id, Long staffId, String staffName, LocalDate leaveDate, String reason, LocalDateTime createdAt) {
        this.id = id;
        this.staffId = staffId;
        this.staffName = staffName;
        this.leaveDate = leaveDate;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
