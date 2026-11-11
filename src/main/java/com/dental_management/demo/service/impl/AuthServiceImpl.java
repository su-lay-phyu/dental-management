package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.entities.Patient;
import com.dental_management.demo.model.entities.RoleName;
import com.dental_management.demo.model.entities.User;
import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.response.AuthResponse;
import com.dental_management.demo.repository.PatientRepository;
import com.dental_management.demo.repository.UserRepository;
import com.dental_management.demo.security.JwtService;
import com.dental_management.demo.security.UserDetailsService;
import com.dental_management.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public AuthResponse signup(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleName.PATIENT);

        User savedUser = userRepository.save(user);

        // Create Patient profile for this User
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setNotes(null);

        patientRepository.save(patient);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(savedUser.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}