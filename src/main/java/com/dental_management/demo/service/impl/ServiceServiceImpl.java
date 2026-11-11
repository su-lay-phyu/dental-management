
package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.ServiceRequest;
import com.dental_management.demo.model.response.CategoryResponse;
import com.dental_management.demo.model.response.ServiceResponse;
import com.dental_management.demo.model.entities.Category;
import com.dental_management.demo.model.entities.ServiceEntity;
import com.dental_management.demo.model.entities.Status;
import com.dental_management.demo.repository.CategoryRepository;
import com.dental_management.demo.repository.ServiceRepository;
import com.dental_management.demo.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ServiceResponse createService(ServiceRequest request) {

        ServiceEntity service = new ServiceEntity();

        service.setServiceCode(request.getServiceCode().trim());
        service.setName(request.getName().trim());
        service.setDescription(request.getDescription());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());

        service.setStatus(
                request.getStatus() == null
                        ? Status.ACTIVE
                        : request.getStatus()
        );

        Category category = getCategory(request.getCategoryId());
        service.setCategory(category);

        ServiceEntity savedService = serviceRepository.save(service);

        return mapToResponse(savedService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices(Status status) {

        List<ServiceEntity> services = serviceRepository.findAll();

        List<ServiceResponse> responses = new ArrayList<>();

        for (ServiceEntity service : services) {

            if (status == null || service.getStatus() == status) {
                responses.add(mapToResponse(service));
            }
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service not found: " + id));

        return mapToResponse(service);
    }

    @Override
    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service not found: " + id));

        service.setServiceCode(request.getServiceCode().trim());
        service.setName(request.getName().trim());
        service.setDescription(request.getDescription());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());

        if (request.getStatus() != null) {
            service.setStatus(request.getStatus());
        }

        Category category = getCategory(request.getCategoryId());
        service.setCategory(category);

        ServiceEntity updatedService = serviceRepository.save(service);

        return mapToResponse(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service not found: " + id));

        serviceRepository.delete(service);
    }

    private Category getCategory(Long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found: " + categoryId));
    }

    private ServiceResponse mapToResponse(ServiceEntity service) {

        ServiceResponse response = new ServiceResponse();

        response.setId(service.getId());
        response.setServiceCode(service.getServiceCode());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setDuration(service.getDuration());
        response.setPrice(service.getPrice());
        response.setStatus(service.getStatus());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(service.getCategory().getId());
        categoryResponse.setName(service.getCategory().getName());

        response.setCategory(categoryResponse);

        return response;
    }
}
