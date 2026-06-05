package com.salon.appointment.controller;

import com.salon.appointment.dto.request.SalonServiceRequest;
import com.salon.appointment.dto.request.ServiceCategoryRequest;
import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.SalonServiceResponse;
import com.salon.appointment.dto.response.ServiceCategoryResponse;
import com.salon.appointment.service.ServiceCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/services")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {

    private final ServiceCatalogService serviceCatalogService;

    public AdminServiceController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>>createCategory(
            @Valid @RequestBody ServiceCategoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created", serviceCatalogService.createCategory(request)));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>>updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody ServiceCategoryRequest request){
        return ResponseEntity.ok(ApiResponse.success("Category updated",
                serviceCatalogService.updateCategory(id, request)));
    }

    @PatchMapping("/categories/{id}/toggle")
    public ResponseEntity<ApiResponse<Void>>toggleCategory(@PathVariable Long id){
        serviceCatalogService.toggleCategoryStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Category status toggles"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalonServiceResponse>>createService(
            @Valid @RequestBody SalonServiceRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Service created", serviceCatalogService.createService(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonServiceResponse>>updateService(
            @PathVariable Long id,
            @Valid @RequestBody SalonServiceRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success("Service updated", serviceCatalogService.updateService(id, request)));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Void>>toggleService(@PathVariable Long id){
        serviceCatalogService.toggleCategoryStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Service status toggled"));
    }
}
