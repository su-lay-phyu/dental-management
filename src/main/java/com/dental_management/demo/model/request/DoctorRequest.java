package com.dental_management.demo.model.request;

import com.dental_management.demo.model.entities.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorRequest {

    @NotBlank(message = "Doctor name is required")
    private String name;

    private String email;

    private String phone;

    private String license;

    private String profileUrl;

    private Status status;

    private List<Long> specialities;
}
