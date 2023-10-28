package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Email.OtpEntity;
import com.example.jwtauthorisedlogin.authorization.*;
import com.example.jwtauthorisedlogin.repository.OtpRepository;
import com.example.jwtauthorisedlogin.user.Role;
import com.example.jwtauthorisedlogin.user.User;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.util.ClassUtils.isPresent;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final OtpService otpService;
    private static final int OTP_EXPIRATION_MINUTE=1;

    public AuthenticationResponse register(RegisterRequest request, Role userRole) {

        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);

        var user=new User();

        if (repository.findByEmail(request.getEmail()).isEmpty()) {

            user = User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .isVerified(false)
                    .role(userRole)
                    .build();
        }
        else{
            user = repository.findByEmail(request.getEmail()).orElseThrow();
            if(user.getIsVerified()){
                return AuthenticationResponse.builder()
                        .token("User already exists")
                        .build();
            }
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIsVerified(false);
            user.setRole(userRole);
            repository.save(user);

        }

        String OTP=GenerateOtp.generateOtp();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(OTP);
        otpEntity.setEmail(request.getEmail());
        otpEntity.setExpirationTime(expirationTime);
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(),OTP);

        repository.save(user);

        return AuthenticationResponse.builder()
                .token("Check your email for OTP")
                .build();

    }
    public AuthenticationResponse verify(VerifyRequest request){

        if (otpRepository.findByEmail(request.getEmail()).isEmpty()) {
            return AuthenticationResponse.builder().token("No OTP generated").build();
        }

        String OTP = otpService.getOtpByEmail(request.getEmail());

        if(!(request.getOtp().equals(OTP))){
            return AuthenticationResponse.builder().token("Incorrect OTP").build();
        }

        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isAfter(otpUser.getExpirationTime())){
            return AuthenticationResponse.builder()
                    .token("OTP Expired")
                    .build();
        }

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        user.setIsVerified(true);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
                   request.getEmail(),
                   request.getPassword()
           )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        if(!(user.getIsVerified())){
            return AuthenticationResponse.builder()
                    .token("User Not Verified")
                    .build();
        }

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
    }




}
