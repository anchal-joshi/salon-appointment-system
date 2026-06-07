package com.salon.appointment.dto.request;

import com.salon.appointment.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class StaffScheduleRequest {

    @NotNull(message = "Day of week is required")
    private com.salon.appointment.enums.DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Working status is required")
    private Boolean isWorking;

    public StaffScheduleRequest() {
    }

    public StaffScheduleRequest(com.salon.appointment.enums.DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Boolean isWorking) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isWorking = isWorking;
    }

    public com.salon.appointment.enums.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean isWorking() {
        return isWorking;
    }

    public void setWorking(Boolean working) {
        isWorking = working;
    }
}
