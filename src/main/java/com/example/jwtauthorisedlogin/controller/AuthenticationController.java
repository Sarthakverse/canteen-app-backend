package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.authorization.*;
import com.example.jwtauthorisedlogin.service.AuthenticationService;
import com.example.jwtauthorisedlogin.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final JavaMailSender javaMailSender;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        String roleString = String.valueOf(request.getRole());

        if (!roleString.equals("USER") && !roleString.equals("ADMIN")) {
            return ResponseEntity.badRequest().build();
        }
        Role userRole = Role.valueOf(roleString);
        return ResponseEntity.ok(service.register(request, userRole));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerifyRequest request
    ) {
        return ResponseEntity.ok(service.verify(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
