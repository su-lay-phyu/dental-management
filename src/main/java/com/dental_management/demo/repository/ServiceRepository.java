package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.ServiceEntity;
import com.dental_management.demo.model.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity,Long> {
    List<ServiceEntity> findByStatus(Status status);

    List<ServiceEntity> findByIdInAndStatus(Collection<Long> ids, Status status);
}
