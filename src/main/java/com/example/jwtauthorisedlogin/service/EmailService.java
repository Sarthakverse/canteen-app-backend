package com.example.jwtauthorisedlogin.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOtpEmail(String toEmail, String OTP) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification");
        message.setText("Your OTP for email verification is: " + OTP);

        javaMailSender.send(message);
    }
}