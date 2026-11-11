package com.dental_management.demo.model.response;

import com.dental_management.demo.model.entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String license;
    private String profileUrl;
    private Status status;
    private List<SpecialtyResponse> specialties;
}
