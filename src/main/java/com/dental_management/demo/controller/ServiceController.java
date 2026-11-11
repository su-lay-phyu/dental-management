
package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.ServiceRequest;
import com.dental_management.demo.model.response.ServiceResponse;
import com.dental_management.demo.model.entities.Status;
import com.dental_management.demo.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody ServiceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceService.createService(request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices(
            @RequestParam(required = false) Status status) {

        return ResponseEntity.ok(serviceService.getAllServices(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {

        return ResponseEntity.ok(
                serviceService.updateService(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {

        serviceService.deleteService(id);

        return ResponseEntity.noContent().build();
    }
}
