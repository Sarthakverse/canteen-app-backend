package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Email.OtpEntity;
import com.example.jwtauthorisedlogin.repository.OtpRepository;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public String getOtpByEmail(String email) {
        OtpEntity otpEntity = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not found for email: " + email));

        return otpEntity.getOtp();
    }
}
