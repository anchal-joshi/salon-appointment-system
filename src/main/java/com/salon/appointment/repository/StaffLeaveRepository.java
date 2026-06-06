package com.salon.appointment.repository;

import com.salon.appointment.entity.StaffLeave;
import com.salon.appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {

    List<StaffLeave>findAllByStaff(User staff);
    List<StaffLeave>findAllByStaffAndLeaveDateGreaterThanEqual(User staff, LocalDate from);
    boolean existsByStaffAndLeaveDate(User staff, LocalDate leaveDate);
    List<StaffLeave>findAllByLeaveDate(LocalDate leaveDate);

}
