package com.dental_management.demo.service.impl;

import com.dental_management.demo.config.BookingSessionConfig;
import com.dental_management.demo.model.entities.*;
import com.dental_management.demo.model.request.AppointmentRequest;
import com.dental_management.demo.model.response.AppointmentResponse;
import com.dental_management.demo.model.response.AvailableSlotResponse;
import com.dental_management.demo.repository.*;
import com.dental_management.demo.service.AppointmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorBranchServiceRepository doctorBranchServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ServiceRepository serviceRepository;
    private final DoctorRepository doctorRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;


    // 1. Get available slots
    @Override
    @Transactional(readOnly = true)
    public AvailableSlotResponse getAvailableSlots(
            Long doctorId,
            Long branchId,
            List<Long> serviceIds,
            LocalDate date) {

        validateServiceIds(serviceIds);
        validateDate(date);

        Branch branch = getActiveBranch(branchId);

        List<ServiceEntity> services =
                getActiveServices(serviceIds);

        int totalDuration = getTotalDuration(services);

        List<Doctor> doctors =
                getAvailableDoctors(branchId, serviceIds);

        if (doctorId != null) {

            doctors = doctors.stream()
                    .filter(doctor ->
                            doctor.getId().equals(doctorId))
                    .collect(Collectors.toList());
        }

        List<LocalTime> morningSlots =
                getAvailableSlotsForSession(
                        doctors,
                        branchId,
                        date,
                        totalDuration,
                        BookingSessionConfig.MORNING_START,
                        BookingSessionConfig.MORNING_END
                );

        List<LocalTime> eveningSlots =
                getAvailableSlotsForSession(
                        doctors,
                        branchId,
                        date,
                        totalDuration,
                        BookingSessionConfig.EVENING_START,
                        BookingSessionConfig.EVENING_END
                );

        List<AvailableSlotResponse.SessionSlots> sessions =
                new ArrayList<>();

        sessions.add(
                new AvailableSlotResponse.SessionSlots(
                        "MORNING",
                        BookingSessionConfig.MORNING_START,
                        BookingSessionConfig.MORNING_END,
                        morningSlots
                )
        );

        sessions.add(
                new AvailableSlotResponse.SessionSlots(
                        "EVENING",
                        BookingSessionConfig.EVENING_START,
                        BookingSessionConfig.EVENING_END,
                        eveningSlots
                )
        );

        return new AvailableSlotResponse(
                date,
                branch.getId(),
                serviceIds,
                totalDuration,
                sessions
        );
    }


    // 2. Create appointment
    @Override
    @Transactional
    public AppointmentResponse createAppointment(
            AppointmentRequest request,
            String userEmail) {

        validateServiceIds(request.getServiceIds());

        validateDate(request.getAppointmentDate());

        if (request.getStartTime() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Appointment start time is required"
            );
        }

        Branch branch =
                getActiveBranch(request.getBranchId());

        List<ServiceEntity> services =
                getActiveServices(request.getServiceIds());

        int totalDuration =
                getTotalDuration(services);

        LocalTime startTime =
                request.getStartTime();

        LocalTime endTime =
                startTime.plusMinutes(totalDuration);

        // Find doctors who can perform ALL selected services
        List<Doctor> doctors =
                getAvailableDoctors(
                        request.getBranchId(),
                        request.getServiceIds()
                );

        // If a specific doctor was selected
        if (request.getDoctorId() != null) {

            doctors = doctors.stream()
                    .filter(doctor ->
                            doctor.getId().equals(request.getDoctorId()))
                    .collect(Collectors.toList());
        }

        // Check which doctors are free at the selected time
        List<Doctor> availableDoctors =
                getAvailableDoctorsForTime(
                        doctors,
                        request.getBranchId(),
                        request.getAppointmentDate(),
                        startTime,
                        endTime
                );

        if (availableDoctors.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Selected time is no longer available"
            );
        }

        // Randomly assign one available doctor
        int randomIndex =
                ThreadLocalRandom.current()
                        .nextInt(availableDoctors.size());

        Doctor selectedDoctor =
                availableDoctors.get(randomIndex);

        // Get logged-in patient
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        ));

        Patient patient =
                patientRepository.findByUser_Id(user.getId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Patient profile not found"
                                ));

        // Create appointment
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(selectedDoctor);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(Status.SCHEDULED);
        appointment.setNotes(request.getNotes());
        appointment.setCreatedAt(Instant.now());

        // Save selected services
        for (ServiceEntity service : services) {

            AppointmentServiceItem item =
                    new AppointmentServiceItem();

            item.setAppointment(appointment);
            item.setServiceEntity(service);
            item.setDuration(service.getDuration());
            item.setPrice(service.getPrice());

            appointment.getServiceItems().add(item);
        }

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(
                savedAppointment,
                services,
                totalDuration
        );
    }


    // 3. Validate service IDs
    private void validateServiceIds(List<Long> serviceIds) {

        if (serviceIds == null || serviceIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select at least one service"
            );
        }

        if (serviceIds.stream().anyMatch(Objects::isNull)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service ID cannot be null"
            );
        }

        if (new HashSet<>(serviceIds).size() != serviceIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Duplicate service IDs are not allowed"
            );
        }
    }


    // 4. Validate appointment date
    private void validateDate(LocalDate date) {

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid appointment date"
            );
        }
    }


    // 5. Get active branch
    private Branch getActiveBranch(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Branch not found"
                        ));

        if (branch.getStatus() != Status.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Branch is not active"
            );
        }

        return branch;
    }


    // 6. Get active services
    private List<ServiceEntity> getActiveServices(
            List<Long> serviceIds) {

        List<ServiceEntity> services =
                serviceRepository.findByIdInAndStatus(
                        serviceIds,
                        Status.ACTIVE
                );

        if (services.size() != serviceIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more services are invalid or inactive"
            );
        }

        return services;
    }


    // 7. Calculate total duration
    private int getTotalDuration(
            List<ServiceEntity> services) {

        int totalDuration = 0;

        for (ServiceEntity service : services) {

            if (service.getDuration() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid service duration"
                );
            }

            totalDuration += service.getDuration();
        }

        return totalDuration;
    }


    // 8. Find doctors who can perform ALL selected services
    private List<Doctor> getAvailableDoctors(
            Long branchId,
            List<Long> serviceIds) {

        List<DoctorBranchService> records =
                doctorBranchServiceRepository
                        .findByBranch_IdAndServiceEntity_IdInAndStatus(
                                branchId,
                                serviceIds,
                                Status.ACTIVE
                        );

        Map<Long, Set<Long>> doctorServices =
                new HashMap<>();

        Map<Long, Doctor> doctorMap =
                new HashMap<>();

        for (DoctorBranchService record : records) {

            Doctor doctor = record.getDoctor();

            Long doctorId = doctor.getId();

            doctorMap.put(doctorId, doctor);

            doctorServices
                    .computeIfAbsent(
                            doctorId,
                            id -> new HashSet<>()
                    )
                    .add(record.getServiceEntity().getId());
        }

        List<Doctor> result = new ArrayList<>();

        for (Long doctorId : doctorServices.keySet()) {

            Doctor doctor = doctorMap.get(doctorId);

            boolean canPerformAllServices =
                    doctorServices.get(doctorId)
                            .containsAll(serviceIds);

            boolean doctorIsActive =
                    doctor.getStatus() == Status.ACTIVE;

            if (canPerformAllServices && doctorIsActive) {
                result.add(doctor);
            }
        }

        return result;
    }


    // 9. Generate available slots for one session
    private List<LocalTime> getAvailableSlotsForSession(
            List<Doctor> doctors,
            Long branchId,
            LocalDate date,
            int totalDuration,
            LocalTime sessionStart,
            LocalTime sessionEnd) {

        List<LocalTime> availableSlots =
                new ArrayList<>();

        LocalTime currentTime = sessionStart;

        while (!currentTime.plusMinutes(totalDuration)
                .isAfter(sessionEnd)) {

            LocalTime endTime =
                    currentTime.plusMinutes(totalDuration);

            List<Doctor> availableDoctors =
                    getAvailableDoctorsForTime(
                            doctors,
                            branchId,
                            date,
                            currentTime,
                            endTime
                    );

            if (!availableDoctors.isEmpty()) {
                availableSlots.add(currentTime);
            }

            currentTime = currentTime.plusMinutes(
                    BookingSessionConfig.SLOT_INTERVAL_MINUTES
            );
        }

        return availableSlots;
    }


    // 10. Check doctor schedule and existing appointments
    private List<Doctor> getAvailableDoctorsForTime(
            List<Doctor> doctors,
            Long branchId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        List<Doctor> availableDoctors =
                new ArrayList<>();

        DayOfWeek dayOfWeek =
                date.getDayOfWeek();

        for (Doctor doctor : doctors) {

            List<DoctorSchedule> schedules =
                    doctorScheduleRepository
                            .findByDoctor_IdAndBranch_IdAndDayOfWeek(
                                    doctor.getId(),
                                    branchId,
                                    dayOfWeek
                            );

            boolean scheduleAvailable = false;

            for (DoctorSchedule schedule : schedules) {

                boolean scheduleCoversTime =
                        !startTime.isBefore(schedule.getStartTime())
                                && !endTime.isAfter(schedule.getEndTime());

                if (scheduleCoversTime) {
                    scheduleAvailable = true;
                    break;
                }
            }

            if (!scheduleAvailable) {
                continue;
            }

            List<Appointment> overlappingAppointments =
                    appointmentRepository.findOverlappingAppointments(
                            doctor.getId(),
                            date,
                            startTime,
                            endTime
                    );

            if (overlappingAppointments.isEmpty()) {
                availableDoctors.add(doctor);
            }
        }

        return availableDoctors;
    }


    // 11. Map entity to response
    private AppointmentResponse mapToResponse(
            Appointment appointment,
            List<ServiceEntity> services,
            int totalDuration) {

        AppointmentResponse response =
                new AppointmentResponse();

        response.setId(appointment.getId());

        response.setPatientId(
                appointment.getPatient().getId()
        );

        response.setDoctorId(
                appointment.getDoctor().getId()
        );

        response.setBranchId(
                appointment.getBranch().getId()
        );

        response.setServiceIds(
                services.stream()
                        .map(ServiceEntity::getId)
                        .collect(Collectors.toList())
        );

        response.setTotalDuration(totalDuration);

        response.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        response.setStartTime(
                appointment.getStartTime()
        );

        response.setEndTime(
                appointment.getEndTime()
        );

        response.setStatus(
                appointment.getStatus()
        );

        response.setNotes(
                appointment.getNotes()
        );

        return response;
    }
}