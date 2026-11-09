package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.entities.DoctorBranchService;
import com.dental_management.demo.model.request.AppointmentRequest;
import com.dental_management.demo.model.response.AppointmentResponse;
import com.dental_management.demo.model.response.AvailableSlotResponse;
import com.dental_management.demo.repository.*;
import com.dental_management.demo.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorBranchServiceRepository doctorBranchServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ServiceRepository serviceRepository;
    @Override
    public AvailableSlotResponse getAvailableSlots(Long doctorId, Long branchId, Long serviceId, LocalDate date) {
        DoctorBranchService doctorBranchService=doctorBranchServiceRepository.findByDoctor_IdAndBranch_IdAndServiceEntity_Id(doctorId,branchId,serviceId).orElse(null);
        return null;
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        return null;
    }
}
