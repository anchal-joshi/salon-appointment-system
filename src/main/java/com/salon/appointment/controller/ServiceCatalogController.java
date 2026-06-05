package com.salon.appointment.controller;

import com.salon.appointment.dto.response.ApiResponse;
import com.salon.appointment.dto.response.SalonServiceResponse;
import com.salon.appointment.dto.response.ServiceCategoryResponse;
import com.salon.appointment.enums.ServiceType;
import com.salon.appointment.service.ServiceCatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    public ServiceCatalogController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ServiceCategoryResponse>>>getAllCategories(
            @RequestParam(required = false)ServiceType type){
        List<ServiceCategoryResponse>result = type !=null
                ? serviceCatalogService.getAllActiveCategoriesByType(type)
                : serviceCatalogService.getAllActiveCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories fetched", result));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryResponse>>getCategoryWithServices(
            @PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success("Category fetched", serviceCatalogService.getCategoryWithServices(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceCategoryResponse>>>getAllServices(){
        return ResponseEntity.ok(ApiResponse.success("Services fetched",
                serviceCatalogService.getAllActiveCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonServiceResponse>>getServiceById(
            @PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success("Service fetched",
                serviceCatalogService.getServiceById(id)));
    }

    @GetMapping("/category/{id}/services")
    public ResponseEntity<ApiResponse<List<SalonServiceResponse>>>getServicesByCategory(
            @PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success("Services fetched",
                serviceCatalogService.getServicesByCategoryId(id)));
    }
}
