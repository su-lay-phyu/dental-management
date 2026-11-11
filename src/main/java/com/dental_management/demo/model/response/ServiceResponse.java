package com.dental_management.demo.model.response;

import com.dental_management.demo.model.entities.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse {

    private Long id;
    private String serviceCode;
    private String name;
    private String description;
    private int duration;
    private int price;
    private Status status;

    private CategoryResponse category;
}
