
package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.DoctorScheduleRequest;
import com.dental_management.demo.model.response.DoctorScheduleResponse;
import com.dental_management.demo.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<DoctorScheduleResponse> createSchedule(
            @Valid @RequestBody DoctorScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponse>> getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponse> getScheduleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleService.getScheduleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.noContent().build();
    }
}
