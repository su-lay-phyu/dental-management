package com.dental_management.demo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="doctors")
@Getter
@Setter
@RequiredArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String profileUrl;
    private String License;
    private String email;
    private String phone;
    private Status status;

}
