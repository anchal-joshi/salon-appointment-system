package com.salon.appointment.repository;

import com.salon.appointment.entity.ServiceCategory;
import com.salon.appointment.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository {
    List<ServiceCategory>findAllByIsActiveTrue();
    List<ServiceCategory>findAllByServiceTypeAndIsActiveTrue(ServiceType serviceType);
    Optional<ServiceCategory>findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
