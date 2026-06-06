package com.salon.appointment.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StaffLeaveRequest {

    @NotNull(message = "Leave date is required")
    @Future(message = "Leave date must be in the future")
    private LocalDate leaveDate;

    private String reason;

    public StaffLeaveRequest() {
    }

    public StaffLeaveRequest(LocalDate leaveDate, String reason) {
        this.leaveDate = leaveDate;
        this.reason = reason;
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
}
