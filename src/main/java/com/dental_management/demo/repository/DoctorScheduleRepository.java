package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule,Long> {
        List<DoctorSchedule> findByDoctorIdAndBranchIdAndDayOfWeek(Long doctorId, Long branchId, DayOfWeek dayOfWeek );
}
