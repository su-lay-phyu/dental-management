package com.dental_management.demo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="branches")
@Getter
@Setter
@RequiredArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String address;
    private Status status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;
}
