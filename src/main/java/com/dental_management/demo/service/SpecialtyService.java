
package com.dental_management.demo.service;

import com.dental_management.demo.model.request.SpecialtyRequest;
import com.dental_management.demo.model.response.SpecialtyResponse;

import java.util.List;

public interface SpecialtyService {

    SpecialtyResponse createSpecialty(SpecialtyRequest request);

    List<SpecialtyResponse> getAllSpecialties();

    SpecialtyResponse getSpecialtyById(Long id);

    SpecialtyResponse updateSpecialty(Long id, SpecialtyRequest request);

    void deleteSpecialty(Long id);
}
