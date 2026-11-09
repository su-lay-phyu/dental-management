package com.dental_management.demo.service;

import com.dental_management.demo.model.request.AppointmentRequest;
import com.dental_management.demo.model.response.AppointmentResponse;
import com.dental_management.demo.model.response.AvailableSlotResponse;

import java.time.LocalDate;

public interface AppointmentService {
    AvailableSlotResponse getAvailableSlots(Long doctorId, Long branchId, Long serviceId, LocalDate date);
    AppointmentResponse createAppointment(AppointmentRequest request );
}
