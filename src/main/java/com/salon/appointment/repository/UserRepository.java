package com.salon.appointment.repository;

import com.salon.appointment.dto.response.UserResponse;
import com.salon.appointment.entity.User;
import com.salon.appointment.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByRole(Role role);
}
