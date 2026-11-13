package com.dental_management.demo.repository;

import com.dental_management.demo.model.entities.EmailVerificationOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailVerificationOtpRepository extends JpaRepository<EmailVerificationOtp, Long> {

    Optional<EmailVerificationOtp> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    void deleteByUser_Id(Long userId);
}