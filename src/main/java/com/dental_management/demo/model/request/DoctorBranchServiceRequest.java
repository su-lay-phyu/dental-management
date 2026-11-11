package com.dental_management.demo.model.request;

import com.dental_management.demo.model.entities.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorBranchServiceRequest {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long serviceId;

    private Status status;
}