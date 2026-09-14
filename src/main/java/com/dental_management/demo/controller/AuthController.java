package com.dental_management.demo.controller;

import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.response.AuthResponse;
import com.dental_management.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?>register(@RequestBody UserRequest request)
    {
        AuthResponse response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
