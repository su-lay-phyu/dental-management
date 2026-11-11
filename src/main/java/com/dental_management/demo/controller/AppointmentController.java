package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.AppointmentRequest;
import com.dental_management.demo.model.response.AppointmentResponse;
import com.dental_management.demo.model.response.AvailableSlotResponse;
import com.dental_management.demo.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/available-slots")
    public ResponseEntity<AvailableSlotResponse> getAvailableSlots(
            @RequestParam(required = false) Long doctorId,
            @RequestParam Long branchId,
            @RequestParam List<Long> serviceIds,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        AvailableSlotResponse response =
                appointmentService.getAvailableSlots(
                        doctorId,
                        branchId,
                        serviceIds,
                        date
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody AppointmentRequest request,
            Authentication authentication
    ) {

        String userEmail = authentication.getName();

        AppointmentResponse response =
                appointmentService.createAppointment(
                        request,
                        userEmail
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}