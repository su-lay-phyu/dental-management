package com.dental_management.demo.service.impl;

import com.dental_management.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Dental Clinic - Email Verification");

        message.setText(
                "Hello,\n\n" +
                        "Your verification OTP is: " + otp + "\n\n" +
                        "This OTP will expire in 5 minutes.\n\n" +
                        "Please do not share this OTP with anyone."
        );

        mailSender.send(message);
    }
}