package com.salon.appointment.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class StaffScheduleResponse {

    private Long id;
    private long staffId;
    private String staffName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isWorking;

    public StaffScheduleResponse() {
    }


    public StaffScheduleResponse(Long id, long staffId, String staffName, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, boolean isWorking) {
        this.id = id;
        this.staffId = staffId;
        this.staffName = staffName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isWorking = isWorking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public DayOfWeek getDayOfWeek() {
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

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
