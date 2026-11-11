package com.dental_management.demo.model.response;


import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class DoctorScheduleResponse {

    private Long id;

    private Long doctorId;
    private String doctorName;

    private Long branchId;
    private String branchName;

    private Long roomId;
    private String roomName;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
}

