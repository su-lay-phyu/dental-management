package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.DoctorBranchService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DoctorBranchServiceRepository extends JpaRepository<DoctorBranchService,Long> {
    Optional<DoctorBranchService> findByDoctor_IdAndBranch_IdAndServiceEntity_Id(Long doctorId, Long branchId, Long serviceId);
}
