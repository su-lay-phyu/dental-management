package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.Doctor;
import com.dental_management.demo.model.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    List<Doctor> findByStatus(Status status);
}
