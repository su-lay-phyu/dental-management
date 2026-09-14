package com.dental_management.demo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;

@Entity
@Table(name="appointments")
@Getter
@Setter
@RequiredArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name="doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;
    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;
    private String notes;
    private Instant createdAt;
}
