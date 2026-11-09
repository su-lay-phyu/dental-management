package com.dental_management.demo.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentRequest
{
    private Long doctorId;
    private Long branchId;
    private Long serviceId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private String notes;
}