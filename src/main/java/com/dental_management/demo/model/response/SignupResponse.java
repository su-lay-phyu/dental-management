package com.dental_management.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {

    private String message;
    private String email;
}