package com.dental_management.demo.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {

    private String email;
    private String otp;
}