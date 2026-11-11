
package com.dental_management.demo.service;

import com.dental_management.demo.model.request.ServiceRequest;
import com.dental_management.demo.model.response.ServiceResponse;
import com.dental_management.demo.model.entities.Status;

import java.util.List;

public interface ServiceService {

    ServiceResponse createService(ServiceRequest request);

    List<ServiceResponse> getAllServices(Status status);

    ServiceResponse getServiceById(Long id);

    ServiceResponse updateService(Long id, ServiceRequest request);

    void deleteService(Long id);
}
