package com.salon.appointment.service;

import com.salon.appointment.dto.request.StaffLeaveRequest;
import com.salon.appointment.dto.request.StaffScheduleRequest;
import com.salon.appointment.dto.response.StaffLeaveResponse;
import com.salon.appointment.dto.response.StaffScheduleResponse;
import com.salon.appointment.entity.StaffLeave;
import com.salon.appointment.entity.StaffSchedule;
import com.salon.appointment.entity.User;
import com.salon.appointment.enums.Role;
import com.salon.appointment.exception.AppException;
import com.salon.appointment.mapper.StaffMapper;
import com.salon.appointment.repository.StaffLeaveRepository;
import com.salon.appointment.repository.StaffScheduleRepository;
import com.salon.appointment.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StaffScheduleService {
    private final StaffScheduleRepository scheduleRepository;
    private final StaffLeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final StaffMapper staffMapper;

    public StaffScheduleService(StaffScheduleRepository scheduleRepository, StaffLeaveRepository leaveRepository, UserRepository userRepository, StaffMapper staffMapper) {
        this.scheduleRepository = scheduleRepository;
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        this.staffMapper = staffMapper;
    }

    public List<StaffScheduleResponse>getMySchedule(User staff){
        return scheduleRepository.findAllByStaff(staff)
                .stream()
                .map(staffMapper::toScheduleResponse)
                .toList();
    }

    public List<StaffLeaveResponse>getMyLeaves(User staff){
        return leaveRepository
                .findAllByStaffAndLeaveDateGreaterThanEqual(staff, LocalDate.now())
                .stream()
                .map(staffMapper::toLeaveResponse)
                .toList();
    }

    @Transactional
    public StaffLeaveResponse applyLeave(User staff, StaffLeaveRequest request){
        if (leaveRepository.existsByStaffAndLeaveDate(staff, request.getLeaveDate())){
            throw new AppException("Leave already applied for this date", HttpStatus.CONFLICT);
        }
        StaffLeave leave = new StaffLeave();
        leave.setStaff(staff);
        leave.setLeaveDate(request.getLeaveDate());
        leave.setReason(request.getReason());
        leaveRepository.save(leave);
        return staffMapper.toLeaveResponse(leave);
    }

    @Transactional
    public void cancelLeave(User staff, Long leaveId){
        StaffLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new AppException("Leave not found", HttpStatus.NOT_FOUND));
        if (!leave.getStaff().getId().equals(staff.getId())){
            throw new AppException("You can only cancel your own leaves", HttpStatus.FORBIDDEN);
        }
        if (leave.getLeaveDate().isBefore(LocalDate.now())){
            throw new AppException("Cannot cancel a past leave", HttpStatus.BAD_REQUEST);
        }
        leaveRepository.delete(leave);
    }

    @Transactional
    public StaffScheduleResponse setSchedule(Long staffId, StaffScheduleRequest request){
        User staff = userRepository.findById(staffId)
                .orElseThrow(()-> new AppException("Not found", HttpStatus.NOT_FOUND));
        if (request.getEndTime().isBefore(request.getStartTime()) ||
        request.getEndTime().equals(request.getStartTime())){
                throw new AppException("Eng time must be after start time", HttpStatus.BAD_REQUEST);
            }
        StaffSchedule schedule = scheduleRepository
                .findByStaffAndDayOfWeek(staff, request.getDayOfWeek())
                .orElseGet(() -> {
                    StaffSchedule s = new StaffSchedule();
                    s.setStaff(staff);
                    return s;
                });
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setWorking(request.isWorking());
        scheduleRepository.save(schedule);
        return staffMapper.toScheduleResponse(schedule);

    }

    public List<StaffScheduleResponse>getStaffSchedule(Long staffId){
        User staff = userRepository.findById(staffId)
                .orElseThrow(()-> new AppException("Not found", HttpStatus.NOT_FOUND));
        return scheduleRepository.findAllByStaff(staff)
                .stream()
                .map(staffMapper::toScheduleResponse)
                .toList();
    }

    public List<StaffLeaveResponse>getLeavesByDate(LocalDate date){
        return leaveRepository.findAllByLeaveDate(date)
                .stream()
                .map(staffMapper::toLeaveResponse)
                .toList();
    }

    public List<StaffLeaveResponse>getAllLeavesOfStaff(Long staffId){
        User staff = userRepository.findById(staffId)
                .orElseThrow(()-> new AppException("Not Found", HttpStatus.NOT_FOUND));
        return leaveRepository.findAllByStaff(staff)
                .stream()
                .map(staffMapper::toLeaveResponse)
                .toList();
    }

    @Transactional
    public void deleteLeave(Long leaveId){
        StaffLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new AppException("Leave not found", HttpStatus.NOT_FOUND));
        leaveRepository.delete(leave);
    }

    //  Helper

    private User getStaffById(Long staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (!staff.getRole().equals(Role.STAFF)) {
            throw new AppException("User is not a staff member", HttpStatus.BAD_REQUEST);
        }
        return staff;
    }
}
