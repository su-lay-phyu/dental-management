
package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.request.SpecialtyRequest;
import com.dental_management.demo.model.response.SpecialtyResponse;
import com.dental_management.demo.model.entities.Specialties;
import com.dental_management.demo.repository.SpecialtiesRepository;
import com.dental_management.demo.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtiesRepository specialtiesRepository;

    @Override
    @Transactional
    public SpecialtyResponse createSpecialty(SpecialtyRequest request) {

        Specialties specialty = new Specialties();
        specialty.setName(request.getName().trim());

        Specialties savedSpecialty =
                specialtiesRepository.save(specialty);

        return mapToResponse(savedSpecialty);
    }

    @Override
    public List<SpecialtyResponse> getAllSpecialties() {

        List<Specialties> specialties =
                specialtiesRepository.findAll();

        List<SpecialtyResponse> responses = new ArrayList<>();

        for (Specialties specialty : specialties) {
            responses.add(mapToResponse(specialty));
        }

        return responses;
    }

    @Override
    public SpecialtyResponse getSpecialtyById(Long id) {

        Specialties specialty = specialtiesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Specialty not found: " + id));

        return mapToResponse(specialty);
    }

    @Override
    @Transactional
    public SpecialtyResponse updateSpecialty(
            Long id, SpecialtyRequest request) {

        Specialties specialty = specialtiesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Specialty not found: " + id));

        specialty.setName(request.getName().trim());

        Specialties updatedSpecialty =
                specialtiesRepository.save(specialty);

        return mapToResponse(updatedSpecialty);
    }

    @Override
    @Transactional
    public void deleteSpecialty(Long id) {

        Specialties specialty = specialtiesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Specialty not found: " + id));

        specialtiesRepository.delete(specialty);
    }

    private SpecialtyResponse mapToResponse(Specialties specialty) {

        SpecialtyResponse response = new SpecialtyResponse();

        response.setId(specialty.getId());
        response.setName(specialty.getName());

        return response;
    }
}
