package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.DoctorBranchServiceRequest;
import com.dental_management.demo.model.response.DoctorBranchServiceResponse;
import com.dental_management.demo.model.entities.Status;
import com.dental_management.demo.service.DoctorBranchServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-branch-services")
@RequiredArgsConstructor
public class DoctorBranchServiceController {

    private final DoctorBranchServiceService doctorBranchServiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorBranchServiceResponse create(
            @Valid @RequestBody DoctorBranchServiceRequest request
    ) {
        return doctorBranchServiceService.create(request);
    }

    @GetMapping
    public List<DoctorBranchServiceResponse> getAll() {
        return doctorBranchServiceService.getAll();
    }

    @GetMapping("/{id}")
    public DoctorBranchServiceResponse getById(
            @PathVariable Long id
    ) {
        return doctorBranchServiceService.getById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<DoctorBranchServiceResponse> getByDoctor(
            @PathVariable Long doctorId
    ) {
        return doctorBranchServiceService.getByDoctor(doctorId);
    }

    @GetMapping("/branch/{branchId}")
    public List<DoctorBranchServiceResponse> getByBranch(
            @PathVariable Long branchId
    ) {
        return doctorBranchServiceService.getByBranch(branchId);
    }

    @GetMapping("/service/{serviceId}")
    public List<DoctorBranchServiceResponse> getByService(
            @PathVariable Long serviceId
    ) {
        return doctorBranchServiceService.getByService(serviceId);
    }

    @GetMapping("/status/{status}")
    public List<DoctorBranchServiceResponse> getByStatus(
            @PathVariable Status status
    ) {
        return doctorBranchServiceService.getByStatus(status);
    }

    @PutMapping("/{id}")
    public DoctorBranchServiceResponse update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorBranchServiceRequest request
    ) {
        return doctorBranchServiceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        doctorBranchServiceService.delete(id);
    }
}