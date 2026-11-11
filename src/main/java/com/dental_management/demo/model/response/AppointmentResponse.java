package com.dental_management.demo.model.response;

import com.dental_management.demo.model.entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentResponse {

    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long branchId;

    private List<Long> serviceIds;

    private int totalDuration;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Status status;

    private String notes;
}