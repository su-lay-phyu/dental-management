
package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.DoctorScheduleRequest;
import com.dental_management.demo.model.response.DoctorScheduleResponse;
import com.dental_management.demo.model.entities.*;
import com.dental_management.demo.repository.*;
import com.dental_management.demo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public DoctorScheduleResponse createSchedule(
            DoctorScheduleRequest request) {

        validateSchedule(request, null);

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        DoctorSchedule schedule = new DoctorSchedule();

        schedule.setDoctor(doctor);
        schedule.setBranch(branch);
        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        DoctorSchedule savedSchedule =
                scheduleRepository.save(schedule);

        return mapToResponse(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> getAllSchedules() {

        List<DoctorSchedule> schedules =
                scheduleRepository.findAll();

        List<DoctorScheduleResponse> responses =
                new ArrayList<>();

        for (DoctorSchedule schedule : schedules) {
            responses.add(mapToResponse(schedule));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorScheduleResponse getScheduleById(Long id) {

        DoctorSchedule schedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Schedule not found: " + id));

        return mapToResponse(schedule);
    }

    @Override
    @Transactional
    public DoctorScheduleResponse updateSchedule(
            Long id,
            DoctorScheduleRequest request) {

        DoctorSchedule schedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Schedule not found: " + id));

        validateSchedule(request, id);

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new RuntimeException("Branch not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        schedule.setDoctor(doctor);
        schedule.setBranch(branch);
        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        DoctorSchedule updatedSchedule =
                scheduleRepository.save(schedule);

        return mapToResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {

        DoctorSchedule schedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Schedule not found: " + id));

        scheduleRepository.delete(schedule);
    }

    private void validateSchedule(
            DoctorScheduleRequest request,
            Long excludeScheduleId) {

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new RuntimeException(
                    "Start time must be before end time");
        }

        // Check whether the room belongs to the selected branch
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        if (!room.getBranch().getId().equals(request.getBranchId())) {
            throw new RuntimeException(
                    "Selected room does not belong to this branch");
        }

        // Check doctor's existing schedules
        List<DoctorSchedule> doctorSchedules =
                scheduleRepository.findByDoctor_IdAndDayOfWeek(
                        request.getDoctorId(),
                        request.getDayOfWeek()
                );

        for (DoctorSchedule existing : doctorSchedules) {

            if (excludeScheduleId != null
                    && existing.getId().equals(excludeScheduleId)) {
                continue;
            }

            if (isOverlapping(
                    request.getStartTime(),
                    request.getEndTime(),
                    existing.getStartTime(),
                    existing.getEndTime())) {

                throw new RuntimeException(
                        "Doctor already has a schedule during this time");
            }
        }

        // Check room's existing schedules
        List<DoctorSchedule> roomSchedules =
                scheduleRepository.findByRoom_IdAndDayOfWeek(
                        request.getRoomId(),
                        request.getDayOfWeek()
                );

        for (DoctorSchedule existing : roomSchedules) {

            if (excludeScheduleId != null
                    && existing.getId().equals(excludeScheduleId)) {
                continue;
            }

            if (isOverlapping(
                    request.getStartTime(),
                    request.getEndTime(),
                    existing.getStartTime(),
                    existing.getEndTime())) {

                throw new RuntimeException(
                        "Room is already occupied during this time");
            }
        }
    }

    private boolean isOverlapping(
            LocalTime newStart,
            LocalTime newEnd,
            LocalTime existingStart,
            LocalTime existingEnd) {

        return newStart.isBefore(existingEnd)
                && newEnd.isAfter(existingStart);
    }

    private DoctorScheduleResponse mapToResponse(
            DoctorSchedule schedule) {

        DoctorScheduleResponse response =
                new DoctorScheduleResponse();

        response.setId(schedule.getId());

        response.setDoctorId(schedule.getDoctor().getId());
        response.setDoctorName(schedule.getDoctor().getName());

        response.setBranchId(schedule.getBranch().getId());
        response.setBranchName(schedule.getBranch().getName());

        response.setRoomId(schedule.getRoom().getId());
        response.setRoomName(schedule.getRoom().getName());

        response.setDayOfWeek(schedule.getDayOfWeek());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());

        return response;
    }
}
