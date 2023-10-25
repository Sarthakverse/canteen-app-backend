package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.authorization.AuthenticationRequest;
import com.example.jwtauthorisedlogin.authorization.AuthenticationResponse;
import com.example.jwtauthorisedlogin.authorization.RegisterRequest;
import com.example.jwtauthorisedlogin.service.AuthenticationService;
import com.example.jwtauthorisedlogin.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        String roleString = String.valueOf(request.getRole());

        if (!roleString.equals("USER") && !roleString.equals("ADMIN")) {
            return ResponseEntity.badRequest().build();
        }
        Role userRole = Role.valueOf(roleString);
        return ResponseEntity.ok(service.register(request, userRole));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
