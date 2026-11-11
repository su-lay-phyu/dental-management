package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.Branch;
import com.dental_management.demo.model.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {
    List<Branch> findByStatus(Status status);
}
