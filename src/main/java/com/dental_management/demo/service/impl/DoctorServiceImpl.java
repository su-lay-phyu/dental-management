
package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.DoctorRequest;
import com.dental_management.demo.model.response.DoctorResponse;
import com.dental_management.demo.model.entities.Doctor;
import com.dental_management.demo.model.entities.Specialties;
import com.dental_management.demo.model.entities.Status;
import com.dental_management.demo.model.response.SpecialtyResponse;
import com.dental_management.demo.repository.DoctorRepository;
import com.dental_management.demo.repository.SpecialtiesRepository;
import com.dental_management.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtiesRepository specialtiesRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {

        Doctor doctor = new Doctor();

        doctor.setName(request.getName().trim());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setLicense(request.getLicense());
        doctor.setProfileUrl(request.getProfileUrl());

        if (request.getStatus() == null) {
            doctor.setStatus(Status.ACTIVE);
        } else {
            doctor.setStatus(request.getStatus());
        }

        doctor.setSpecialties(getSpecialtiesByIds(request.getSpecialities()));

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponse(savedDoctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors(Status status) {

        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorResponse> responses = new ArrayList<>();

        for (Doctor doctor : doctors) {

            if (status == null || doctor.getStatus() == status) {
                responses.add(mapToResponse(doctor));
            }
        }

        return responses;
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found: " + id));

        return mapToResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found: " + id));

        doctor.setName(request.getName().trim());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setLicense(request.getLicense());
        doctor.setProfileUrl(request.getProfileUrl());

        if (request.getStatus() != null) {
            doctor.setStatus(request.getStatus());
        }

        if (request.getSpecialities() != null) {
            doctor.setSpecialties(getSpecialtiesByIds(request.getSpecialities()));
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }

    @Override
    @Transactional
    public void updateDoctorStatus(Long id, Status status) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found: " + id));

        if (status != Status.ACTIVE && status != Status.INACTIVE) {
            throw new RuntimeException(
                    "Doctor status must be ACTIVE or INACTIVE"
            );
        }

        doctor.setStatus(status);

        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found: " + id));

        doctorRepository.delete(doctor);
    }

    private List<Specialties> getSpecialtiesByIds(List<Long> specialtyIds) {

        List<Specialties> specialties = new ArrayList<>();
        if (specialtyIds == null) {
            return specialties;
        }

        for (Long specialtyId : specialtyIds)
        {
            Specialties specialty = specialtiesRepository.findById(specialtyId).orElseThrow(() -> new RuntimeException("Specialty not found: " + specialtyId));
            specialties.add(specialty);
        }
        return specialties;
    }


    private DoctorResponse mapToResponse(Doctor doctor) {

        DoctorResponse response = new DoctorResponse();

        response.setId(doctor.getId());
        response.setName(doctor.getName());
        response.setEmail(doctor.getEmail());
        response.setPhone(doctor.getPhone());
        response.setLicense(doctor.getLicense());
        response.setProfileUrl(doctor.getProfileUrl());
        response.setStatus(doctor.getStatus());

        List<SpecialtyResponse> specialtyResponses = new ArrayList<>();

        for (Specialties specialty : doctor.getSpecialties()) {

            SpecialtyResponse specialtyResponse = new SpecialtyResponse();

            specialtyResponse.setId(specialty.getId());
            specialtyResponse.setName(specialty.getName());

            specialtyResponses.add(specialtyResponse);
        }

        response.setSpecialties(specialtyResponses);

        return response;
    }

}
