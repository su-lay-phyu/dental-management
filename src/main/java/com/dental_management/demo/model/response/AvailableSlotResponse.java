package com.dental_management.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AvailableSlotResponse {

    private LocalDate date;

    private Long doctorId;

    private Long branchId;

    private Long serviceId;

    private List<LocalTime> availableSlots;
}
