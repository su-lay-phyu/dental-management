package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.DoctorSchedule;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctor_IdAndBranch_IdAndDayOfWeek(Long doctorId, Long branchId,DayOfWeek dayOfWeek);
    List<DoctorSchedule> findByRoom_IdAndDayOfWeek(Long roomId, DayOfWeek dayOfWeek);
    @Query("SELECT ds FROM DoctorSchedule ds " +
            "WHERE ds.doctor.id = :doctorId " +
            "AND ds.dayOfWeek = :dayOfWeek")
    List<DoctorSchedule> findByDoctor_IdAndDayOfWeek(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}