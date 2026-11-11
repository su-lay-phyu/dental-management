package com.dental_management.demo.model.response;

import com.dental_management.demo.model.entities.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorBranchServiceResponse {

    private Long id;

    private Long doctorId;
    private String doctorName;

    private Long branchId;
    private String branchName;

    private Long serviceId;
    private String serviceName;

    private Status status;
}
