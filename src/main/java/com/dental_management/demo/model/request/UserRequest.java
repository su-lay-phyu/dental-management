package com.dental_management.demo.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
}
