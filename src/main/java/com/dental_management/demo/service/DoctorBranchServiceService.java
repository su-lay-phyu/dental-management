package com.dental_management.demo.service;

import com.dental_management.demo.model.request.DoctorBranchServiceRequest;
import com.dental_management.demo.model.response.DoctorBranchServiceResponse;
import com.dental_management.demo.model.entities.Status;

import java.util.List;

public interface DoctorBranchServiceService {

    DoctorBranchServiceResponse create(
            DoctorBranchServiceRequest request
    );

    List<DoctorBranchServiceResponse> getAll();

    List<DoctorBranchServiceResponse> getByDoctor(Long doctorId);

    List<DoctorBranchServiceResponse> getByBranch(Long branchId);

    List<DoctorBranchServiceResponse> getByService(Long serviceId);

    List<DoctorBranchServiceResponse> getByStatus(Status status);

    DoctorBranchServiceResponse getById(Long id);

    DoctorBranchServiceResponse update(
            Long id,
            DoctorBranchServiceRequest request
    );

    void delete(Long id);
}