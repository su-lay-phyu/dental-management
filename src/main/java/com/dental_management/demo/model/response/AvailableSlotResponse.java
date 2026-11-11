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

    private Long branchId;

    private List<Long> serviceIds;

    private int totalDuration;

    private List<SessionSlots> sessions;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SessionSlots {

        private String sessionName;

        private LocalTime startTime;

        private LocalTime endTime;

        private List<LocalTime> availableSlots;
    }
}