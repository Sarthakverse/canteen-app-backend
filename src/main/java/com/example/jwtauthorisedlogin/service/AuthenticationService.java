package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.authorization.AuthenticationRequest;
import com.example.jwtauthorisedlogin.authorization.AuthenticationResponse;
import com.example.jwtauthorisedlogin.authorization.RegisterRequest;
import com.example.jwtauthorisedlogin.user.Role;
import com.example.jwtauthorisedlogin.user.User;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //add logic here for generating random string token that is used
    // to send the email for resetting password


    public AuthenticationResponse register(RegisterRequest request, Role userRole) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().token("User already registered").build();
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

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

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
    }


}
