package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :appointmentDate AND a.status <> com.dental_management.demo.model.entities.Status.CANCELLED")
    List<Appointment> findBookedAppointments(@Param("doctorId") Long doctorId, @Param("appointmentDate") LocalDate appointmentDate );
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :appointmentDate AND a.startTime < :endTime AND a.endTime > :startTime AND a.status <> com.dental_management.demo.model.entities.Status.CANCELLED ")
    List<Appointment> findOverlappingAppointments(@Param("doctorId") Long doctorId, @Param("appointmentDate") LocalDate appointmentDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime );

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.patient.id = :patientId
    AND a.appointmentDate = :appointmentDate
    AND a.startTime < :endTime
    AND a.endTime > :startTime
    AND a.status <> com.dental_management.demo.model.entities.Status.CANCELLED
""")
    List<Appointment> findPatientOverlappingAppointments(
            @Param("patientId") Long patientId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
