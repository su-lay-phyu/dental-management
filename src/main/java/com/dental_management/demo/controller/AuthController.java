package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.ResendOtpRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.request.VerifyOtpRequest;
import com.dental_management.demo.model.response.AuthResponse;
import com.dental_management.demo.model.response.SignupResponse;
import com.dental_management.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody UserRequest request) {

        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<SignupResponse> resendOtp(
            @RequestBody ResendOtpRequest request) {

        return ResponseEntity.ok(authService.resendOtp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}