package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.DoctorBranchService;
import com.dental_management.demo.model.entities.Status;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorBranchServiceRepository
        extends JpaRepository<DoctorBranchService, Long> {

    Optional<DoctorBranchService> findByDoctor_IdAndBranch_IdAndServiceEntity_Id(Long doctorId, Long branchId, Long serviceId);

    List<DoctorBranchService> findByBranch_IdAndServiceEntity_IdInAndStatus(Long branchId, Collection<Long> serviceIds, Status status);

    boolean existsByDoctor_IdAndBranch_IdAndServiceEntity_Id(Long doctorId, Long branchId, Long serviceId);

    boolean existsByDoctor_IdAndBranch_IdAndServiceEntity_IdAndIdNot(Long doctorId, Long branchId, Long serviceId,Long excludeId);

    List<DoctorBranchService> findByDoctor_Id(Long doctorId);

    List<DoctorBranchService> findByBranch_Id(Long branchId);

    List<DoctorBranchService> findByServiceEntity_Id(Long serviceId);

    List<DoctorBranchService> findByStatus(Status status);

}