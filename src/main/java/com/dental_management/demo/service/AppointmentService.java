package com.dental_management.demo.service;

import com.dental_management.demo.model.request.AppointmentRequest;
import com.dental_management.demo.model.response.AppointmentResponse;
import com.dental_management.demo.model.response.AvailableSlotResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AvailableSlotResponse getAvailableSlots(
            Long doctorId,
            Long branchId,
            List<Long> serviceIds,
            LocalDate date
    );

    AppointmentResponse createAppointment(
            AppointmentRequest request,
            String userEmail
    );
}