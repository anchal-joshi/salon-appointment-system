package com.salon.appointment.repository;

import com.salon.appointment.entity.StaffSchedule;
import com.salon.appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Long> {
    List<StaffSchedule>findAllByStaff(User staff);
    Optional<StaffSchedule>findByStaffAndDayOfWeek(User staff, DayOfWeek dayOfWeek);
    boolean existsByStaffAndDayOfWeek(User staff, DayOfWeek dayOfWeek);
}
