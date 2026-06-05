package com.salon.appointment.repository;

import com.salon.appointment.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {
    List<SalonService>findAllByCategoryIdAndIsActiveTrue(Long categoryId);
    List<SalonService>findAllByIsActiveTrue();
    boolean existsByNameIgnoreCaseAndCategoryId(String name, Long categoryId);
}
