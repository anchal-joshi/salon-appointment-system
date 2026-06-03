package com.salon.appointment.mapper;

import com.salon.appointment.dto.request.ServiceCategoryRequest;
import com.salon.appointment.dto.response.SalonServiceResponse;
import com.salon.appointment.dto.response.ServiceCategoryResponse;
import com.salon.appointment.entity.SalonService;
import com.salon.appointment.entity.ServiceCategory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ServiceMapper {
    public SalonServiceResponse toServiceResponse(SalonService service){
        SalonServiceResponse response = new SalonServiceResponse();
        response.setId(service.getId());
        response.setCategoryId(service.getCategory().getId());
        response.setName(service.getName());
        response.setCategoryName(service.getCategory().getName());
        response.setDescription(service.getDescription());
        response.setActive(service.isActive());
        response.setPrice(service.getPrice());
        response.setCreatedAt(service.getCreatedAt());
        response.setDurationMinutes(service.getDurationMinutes());
        return response;
    }

    public ServiceCategoryResponse toCategoryResponse(ServiceCategory category,
                                                      boolean includeServices){
        List<SalonServiceResponse> services = includeServices && category.getServices() != null
                ? category.getServices().stream()
                .filter(SalonService::isActive)
                .map(this::toServiceResponse)
                .toList()
                : Collections.emptyList();
        ServiceCategoryResponse response = new ServiceCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setActive(category.isActive());
        response.setDescription(category.getDescription());
        response.setServices(services);
        response.setServiceType(category.getServiceType());
        response.setCreatedAt(category.getCreatedAt());
        return response;
    }
}
