package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.*;
import com.example.jwtauthorisedlogin.service.AuthenticationService;
import com.example.jwtauthorisedlogin.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        String roleString = String.valueOf(request.getRole());

        if (!roleString.equals("USER") && !roleString.equals("ADMIN")) {
            return ResponseEntity.badRequest().build();
        }
        Role userRole = Role.valueOf(roleString);
        return service.register(request,userRole);
       // return ResponseEntity.ok(service.register(request, userRole));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerifyRequest request
    ) {
        return service.verify(request);
        //return ResponseEntity.ok(service.verify(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return service.authenticate(request);
        //return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthenticationResponse> forgot(
            @RequestBody ForgotPasswordRequest request
    ){
        return service.forgot(request);
//        return ResponseEntity.ok(service.forgot(request));
    }

    @PostMapping("/reset-password-verify")
    public ResponseEntity<AuthenticationResponse> reset(
            @RequestBody verifyResetPasswordRequest request
    ){
        return service.resetVerifyOtp(request);
        // return ResponseEntity.ok(service.resetVerifyOtp(request));

    }

    @PostMapping("/reset-new-password")
    public ResponseEntity<AuthenticationResponse> resetNewPassword(@RequestBody PasswordResetRequest request){
        return service.resetPassword(request);
        //return ResponseEntity.ok(service.resetPassword(request));
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<AuthenticationResponse> resend(
            @RequestBody ResendRequest request
    ){
        return service.resend(request);
//        return ResponseEntity.ok(service.resend(request));
    }

}
