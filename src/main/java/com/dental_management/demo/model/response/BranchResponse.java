package com.dental_management.demo.model.response;

import com.dental_management.demo.model.entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BranchResponse {

    private Long id;

    private String name;

    private String phone;

    private String address;

    private Status status;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Long cityId;

    private String cityName;
}