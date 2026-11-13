package com.dental_management.demo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private RoleName role;
    @Column(nullable = false)
    private boolean emailVerified = false;
}
