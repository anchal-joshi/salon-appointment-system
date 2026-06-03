package com.salon.appointment.dto.response;

import com.salon.appointment.dto.request.SalonServiceRequest;
import com.salon.appointment.enums.ServiceType;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private ServiceType serviceType;
    private boolean isActive;
    private LocalDateTime createdAt;
    private List<SalonServiceResponse>services;

    public ServiceCategoryResponse() {
    }

    public ServiceCategoryResponse(Long id, String name, String description, ServiceType serviceType, boolean isActive, LocalDateTime createdAt, List<SalonServiceResponse> services) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serviceType = serviceType;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.services = services;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SalonServiceResponse> getServices() {
        return services;
    }

    public void setServices(List<SalonServiceResponse> services) {
        this.services = services;
    }
}
