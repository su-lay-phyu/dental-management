package com.dental_management.demo.service;

import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.response.AuthResponse;

public interface AuthService {
    AuthResponse signup(UserRequest request);

    AuthResponse login(LoginRequest request);
}
