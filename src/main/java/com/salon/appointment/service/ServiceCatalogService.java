package com.salon.appointment.service;

import com.salon.appointment.dto.request.SalonServiceRequest;
import com.salon.appointment.dto.request.ServiceCategoryRequest;
import com.salon.appointment.dto.response.SalonServiceResponse;
import com.salon.appointment.dto.response.ServiceCategoryResponse;
import com.salon.appointment.entity.SalonService;
import com.salon.appointment.entity.ServiceCategory;
import com.salon.appointment.enums.ServiceType;
import com.salon.appointment.exception.AppException;
import com.salon.appointment.mapper.ServiceMapper;
import com.salon.appointment.repository.SalonServiceRepository;
import com.salon.appointment.repository.ServiceCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceCatalogService {

    private final ServiceCategoryRepository categoryRepository;
    private final SalonServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;


    public ServiceCatalogService(ServiceCategoryRepository categoryRepository, SalonServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    public List<ServiceCategoryResponse>getAllActiveCategories(){
        return categoryRepository.findAllByIsActiveTrue()
                .stream()
                .map(c -> serviceMapper.toCategoryResponse(c, false))
                .toList();
    }

    public List<ServiceCategoryResponse>getAllActiveCategoriesByType(ServiceType type){
        return categoryRepository.findAllByServiceTypeAndIsActiveTrue(type)
                .stream()
                .map(c -> serviceMapper.toCategoryResponse(c, false))
                .toList();
    }

    public ServiceCategoryResponse getCategoryWithServices(Long categoryId){
        ServiceCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException("Category not found",
                        HttpStatus.NOT_FOUND));
        return serviceMapper.toCategoryResponse(category, true);
    }

    public SalonServiceResponse getServiceById(Long serviceId){
        SalonService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException("Service not found", HttpStatus.NOT_FOUND));
        return serviceMapper.toServiceResponse(service);
    }

    @Transactional
    public ServiceCategoryResponse createCategory(ServiceCategoryRequest request){
        if (categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new AppException("Category with this name already exists", HttpStatus.NOT_FOUND);
        }
        ServiceCategory category = new ServiceCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setServiceType(request.getServiceType());
        category.setActive(true);
        categoryRepository.save(category);
        return serviceMapper.toCategoryResponse(category, false);
    }

    @Transactional
    public ServiceCategoryResponse updateCategory(Long id, ServiceCategoryRequest request){
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        categoryRepository.findByNameIgnoreCase(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)){
                        throw new AppException("Category name already in use", HttpStatus.CONFLICT);
                    }
                });
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setServiceType(request.getServiceType());
        categoryRepository.save(category);
        return serviceMapper.toCategoryResponse(category, false);
    }

    @Transactional
    public void toggleCategoryStatus(Long id){
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() ->new AppException("Category not found", HttpStatus.NOT_FOUND));
        category.setActive(!category.isActive());
        categoryRepository.save(category);
    }

    //ADMIN: Service CRUD
    @Transactional
    public SalonServiceResponse createService(SalonServiceRequest request){
        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        if (serviceRepository.existsByNameIgnoreCaseAndCategoryId(
                request.getName(), request.getCategoryId()
        )){
            throw new AppException("Service with this name already exists in the category", HttpStatus.CONFLICT);
        }

        SalonService service = new SalonService();
        service.setCategory(category);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setActive(true);
        serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }

    @Transactional
    public SalonServiceResponse updateService(Long id, SalonServiceRequest request){
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException("Service not found", HttpStatus.NOT_FOUND));

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        service.setCategory(category);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());
        serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }

    @Transactional
    public void toggleServiceStatus(Long id){
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException("Service not found", HttpStatus.NOT_FOUND));
        service.setActive(!service.isActive());
        serviceRepository.save(service);
    }

    public List<SalonServiceResponse>getServicesByCategoryId(Long categoryId){
        if (!categoryRepository.existsById(categoryId)){
            throw new AppException("Category not found", HttpStatus.NOT_FOUND);
        }
        return serviceRepository.findAllByCategoryIdAndIsActiveTrue(categoryId)
                .stream()
                .map(serviceMapper::toServiceResponse)
                .toList();
    }
}
