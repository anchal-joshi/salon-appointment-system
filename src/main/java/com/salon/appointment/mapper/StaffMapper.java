package com.salon.appointment.mapper;

import com.salon.appointment.dto.response.StaffLeaveResponse;
import com.salon.appointment.dto.response.StaffScheduleResponse;
import com.salon.appointment.entity.StaffLeave;
import com.salon.appointment.entity.StaffSchedule;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {


    public StaffScheduleResponse toScheduleResponse(StaffSchedule schedule){
        StaffScheduleResponse response = new StaffScheduleResponse();
        response.setId(schedule.getId());
        response.setStaffId(schedule.getId());
        response.setStaffName(schedule.getStaff().getName());
        response.setDayOfWeek(schedule.getDayOfWeek());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setWorking(schedule.isWorking());
        return response;
    }

    public StaffLeaveResponse toLeaveResponse(StaffLeave leave){
        StaffLeaveResponse response = new StaffLeaveResponse();
        response.setId(leave.getId());
        response.setStaffId(leave.getStaff().getId());
        response.setStaffName(leave.getStaff().getName());
        response.setLeaveDate(leave.getLeaveDate());
        response.setReason(leave.getReason());
        response.setCreatedAt(leave.getCreatedAt());
        return response;
    }
}
