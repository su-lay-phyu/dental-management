package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.entities.EmailVerificationOtp;
import com.dental_management.demo.model.entities.Patient;
import com.dental_management.demo.model.entities.RoleName;
import com.dental_management.demo.model.entities.User;
import com.dental_management.demo.model.request.LoginRequest;
import com.dental_management.demo.model.request.ResendOtpRequest;
import com.dental_management.demo.model.request.UserRequest;
import com.dental_management.demo.model.request.VerifyOtpRequest;
import com.dental_management.demo.model.response.AuthResponse;
import com.dental_management.demo.model.response.SignupResponse;
import com.dental_management.demo.repository.EmailVerificationOtpRepository;
import com.dental_management.demo.repository.PatientRepository;
import com.dental_management.demo.repository.UserRepository;
import com.dental_management.demo.security.JwtService;
import com.dental_management.demo.security.UserDetailsService;
import com.dental_management.demo.service.AuthService;
import com.dental_management.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PatientRepository patientRepository;
    private final EmailVerificationOtpRepository otpRepository;
    private final EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public SignupResponse signup(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleName.ROLE_PATIENT);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        // Generate 6-digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1000000)
        );

        // Delete previous OTP if exists
        otpRepository.deleteByUser_Id(savedUser.getId());

        EmailVerificationOtp otpEntity = new EmailVerificationOtp();

        otpEntity.setUser(savedUser);

        // Store hashed OTP instead of plain text
        otpEntity.setOtp(passwordEncoder.encode(otp));

        otpEntity.setCreatedAt(Instant.now());
        otpEntity.setExpiresAt(Instant.now().plusSeconds(300));

        otpRepository.save(otpEntity);

        // Send OTP email
        emailService.sendOtp(savedUser.getEmail(), otp);

        return new SignupResponse(
                "OTP has been sent to your email",
                savedUser.getEmail()
        );
    }

    @Override
    @Transactional
    public SignupResponse resendOtp(ResendOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        Instant now = Instant.now();

        // Find previous OTP
        EmailVerificationOtp existingOtp = otpRepository
                .findTopByUser_IdOrderByCreatedAtDesc(user.getId())
                .orElse(null);

        // Allow resend only after 60 seconds
        if (existingOtp != null) {

            long secondsSinceLastOtp =
                    Duration.between(existingOtp.getCreatedAt(), now).getSeconds();

            if (secondsSinceLastOtp < 60) {
                throw new RuntimeException(
                        "Please wait before requesting a new OTP"
                );
            }
        }

        // Delete old OTP
        otpRepository.deleteByUser_Id(user.getId());

        // Generate new 6-digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1000000)
        );

        EmailVerificationOtp newOtp = new EmailVerificationOtp();

        newOtp.setUser(user);
        newOtp.setOtp(passwordEncoder.encode(otp));
        newOtp.setCreatedAt(now);
        newOtp.setExpiresAt(now.plusSeconds(300));

        otpRepository.save(newOtp);

        // Send new OTP
        emailService.sendOtp(user.getEmail(), otp);

        return new SignupResponse(
                "A new OTP has been sent to your email",
                user.getEmail()
        );
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        EmailVerificationOtp otpEntity =
                otpRepository.findTopByUser_IdOrderByCreatedAtDesc(user.getId())
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (Instant.now().isAfter(otpEntity.getExpiresAt())) {
            otpRepository.delete(otpEntity);
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordEncoder.matches(request.getOtp(), otpEntity.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Verify email
        user.setEmailVerified(true);
        userRepository.save(user);

        // Create Patient profile after successful verification
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setNotes(null);

        patientRepository.save(patient);

        // Delete used OTP
        otpRepository.delete(otpEntity);

        // Generate JWT
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
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

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

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