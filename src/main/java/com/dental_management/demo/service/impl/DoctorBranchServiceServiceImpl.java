package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.DoctorBranchServiceRequest;
import com.dental_management.demo.model.response.DoctorBranchServiceResponse;
import com.dental_management.demo.model.entities.*;
import com.dental_management.demo.repository.*;
import com.dental_management.demo.service.DoctorBranchServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorBranchServiceServiceImpl
        implements DoctorBranchServiceService {

    private final DoctorBranchServiceRepository doctorBranchServiceRepository;
    private final DoctorRepository doctorRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public DoctorBranchServiceResponse create(
            DoctorBranchServiceRequest request
    ) {
        validateAssignment(request, null);

        Doctor doctor = getDoctor(request.getDoctorId());
        Branch branch = getBranch(request.getBranchId());
        ServiceEntity serviceEntity = getService(request.getServiceId());

        DoctorBranchService assignment = new DoctorBranchService();

        assignment.setDoctor(doctor);
        assignment.setBranch(branch);
        assignment.setServiceEntity(serviceEntity);

        assignment.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : Status.ACTIVE
        );

        DoctorBranchService saved =
                doctorBranchServiceRepository.save(assignment);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorBranchServiceResponse> getAll() {

        List<DoctorBranchService> assignments =
                doctorBranchServiceRepository.findAll();

        List<DoctorBranchServiceResponse> responses =
                new ArrayList<>();

        for (DoctorBranchService assignment : assignments) {
            responses.add(mapToResponse(assignment));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorBranchServiceResponse> getByDoctor(Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Doctor not found"
            );
        }

        List<DoctorBranchService> assignments =
                doctorBranchServiceRepository.findByDoctor_Id(doctorId);

        return mapToResponseList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorBranchServiceResponse> getByBranch(Long branchId) {

        if (!branchRepository.existsById(branchId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Branch not found"
            );
        }

        List<DoctorBranchService> assignments =
                doctorBranchServiceRepository.findByBranch_Id(branchId);

        return mapToResponseList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorBranchServiceResponse> getByService(Long serviceId) {

        if (!serviceRepository.existsById(serviceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Service not found"
            );
        }

        List<DoctorBranchService> assignments =
                doctorBranchServiceRepository
                        .findByServiceEntity_Id(serviceId);

        return mapToResponseList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorBranchServiceResponse> getByStatus(Status status) {

        List<DoctorBranchService> assignments =
                doctorBranchServiceRepository.findByStatus(status);

        return mapToResponseList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorBranchServiceResponse getById(Long id) {

        DoctorBranchService assignment = getAssignment(id);

        return mapToResponse(assignment);
    }

    @Override
    @Transactional
    public DoctorBranchServiceResponse update(
            Long id,
            DoctorBranchServiceRequest request
    ) {
        DoctorBranchService assignment = getAssignment(id);

        validateAssignment(request, id);

        Doctor doctor = getDoctor(request.getDoctorId());
        Branch branch = getBranch(request.getBranchId());
        ServiceEntity serviceEntity = getService(request.getServiceId());

        assignment.setDoctor(doctor);
        assignment.setBranch(branch);
        assignment.setServiceEntity(serviceEntity);

        if (request.getStatus() != null) {
            assignment.setStatus(request.getStatus());
        }

        DoctorBranchService updated =
                doctorBranchServiceRepository.save(assignment);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        DoctorBranchService assignment = getAssignment(id);

        doctorBranchServiceRepository.delete(assignment);
    }

    // ---------------- Helper Methods ----------------

    private void validateAssignment(
            DoctorBranchServiceRequest request,
            Long excludeId
    ) {

        boolean duplicate;

        if (excludeId == null) {
            duplicate =
                    doctorBranchServiceRepository.existsByDoctor_IdAndBranch_IdAndServiceEntity_Id(
                                    request.getDoctorId(),
                                    request.getBranchId(),
                                    request.getServiceId()
                            );
        } else {
            duplicate =
                    doctorBranchServiceRepository.existsByDoctor_IdAndBranch_IdAndServiceEntity_IdAndIdNot(
                                    request.getDoctorId(),
                                    request.getBranchId(),
                                    request.getServiceId(),
                                    excludeId
                            );
        }

        if (duplicate) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This doctor is already assigned to this service at this branch"
            );
        }
    }

    private Doctor getDoctor(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Doctor not found"
                ));

        if (doctor.getStatus() != Status.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot assign an inactive doctor"
            );
        }

        return doctor;
    }

    private Branch getBranch(Long branchId) {

        return branchRepository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Branch not found"
                ));
    }

    private ServiceEntity getService(Long serviceId) {

        ServiceEntity serviceEntity = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Service not found"
                ));

        if (serviceEntity.getStatus() != Status.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot assign an inactive service"
            );
        }

        return serviceEntity;
    }

    private DoctorBranchService getAssignment(Long id) {

        return doctorBranchServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Doctor branch service assignment not found"
                ));
    }

    private List<DoctorBranchServiceResponse> mapToResponseList(
            List<DoctorBranchService> assignments
    ) {

        List<DoctorBranchServiceResponse> responses =
                new ArrayList<>();

        for (DoctorBranchService assignment : assignments) {
            responses.add(mapToResponse(assignment));
        }

        return responses;
    }

    private DoctorBranchServiceResponse mapToResponse(
            DoctorBranchService assignment
    ) {

        DoctorBranchServiceResponse response =
                new DoctorBranchServiceResponse();

        response.setId(assignment.getId());

        response.setDoctorId(assignment.getDoctor().getId());
        response.setDoctorName(assignment.getDoctor().getName());

        response.setBranchId(assignment.getBranch().getId());
        response.setBranchName(assignment.getBranch().getName());

        response.setServiceId(assignment.getServiceEntity().getId());
        response.setServiceName(assignment.getServiceEntity().getName());

        response.setStatus(assignment.getStatus());

        return response;
    }
}