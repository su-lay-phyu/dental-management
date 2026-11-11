package com.dental_management.demo.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentRequest {

    private Long doctorId;

    private Long branchId;

    private List<Long> serviceIds;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private String notes;
}