
package com.dental_management.demo.service;

import com.dental_management.demo.model.request.DoctorScheduleRequest;
import com.dental_management.demo.model.response.DoctorScheduleResponse;

import java.util.List;

public interface DoctorScheduleService {

    DoctorScheduleResponse createSchedule(DoctorScheduleRequest request);

    List<DoctorScheduleResponse> getAllSchedules();

    DoctorScheduleResponse getScheduleById(Long id);

    DoctorScheduleResponse updateSchedule(
            Long id,
            DoctorScheduleRequest request
    );

    void deleteSchedule(Long id);
}
