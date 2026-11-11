
package com.dental_management.demo.service;

import com.dental_management.demo.model.request.DoctorRequest;
import com.dental_management.demo.model.response.DoctorResponse;
import com.dental_management.demo.model.entities.Status;

import java.util.List;

public interface DoctorService {

    DoctorResponse createDoctor(DoctorRequest request);

    List<DoctorResponse> getAllDoctors(Status status);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void updateDoctorStatus(Long id, Status status);

    void deleteDoctor(Long id);
}
