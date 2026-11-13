package com.dental_management.demo.service;

import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.ResendOtpRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.request.VerifyOtpRequest;
import com.dental_management.demo.model.response.AuthResponse;
import com.dental_management.demo.model.response.SignupResponse;

public interface AuthService {
    SignupResponse signup(UserRequest request);

    SignupResponse resendOtp(ResendOtpRequest request);

    AuthResponse verifyOtp(VerifyOtpRequest request);

    AuthResponse login(LoginRequest request);
}
