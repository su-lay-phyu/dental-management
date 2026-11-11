package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.Specialties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtiesRepository extends JpaRepository<Specialties,Long> {
}
