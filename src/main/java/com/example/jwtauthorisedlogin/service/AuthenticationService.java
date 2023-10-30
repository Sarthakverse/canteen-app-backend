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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;

    private static final int OTP_EXPIRATION_MINUTE=10;
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private final Pattern emailPattern = Pattern.compile(emailRegex);
    public AuthenticationResponse register(RegisterRequest request, Role userRole) {

        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);

        var user=new User();

        if (!isValidEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .token("Invalid email format")
                    .build();
        }

        if (repository.findByEmail(request.getEmail()).isEmpty()) {

            user = User.builder()
                    .fullName(request.getFullName().trim())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .isVerified(false)
                    .role(userRole)
                    .build();
        }
        else{
            user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException(("user not found in database")+request.getEmail()));
            if(user.getIsVerified()){
                return AuthenticationResponse.builder()
                        .token("User already exists")
                        .build();
            }
            user.setFullName(request.getFullName().trim());
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

    private boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
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

        var userOptional = repository.findByEmail(request.getEmail());

        if (!isValidEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .token("Invalid email format")
                    .build();
        }

        if(userOptional.isEmpty()){
            return AuthenticationResponse.builder()
                    .token("User not Registered")
                    .build();
        }

        var user = userOptional.get();
           try{
               authenticationManager.authenticate(
                       new UsernamePasswordAuthenticationToken(
                               request.getEmail(),
                               request.getPassword()
                       )
               );
           }
           catch(Exception e)
           {
               return  AuthenticationResponse.builder()
                       .token("Invalid Credentials")
                       .build();
           }

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


    public AuthenticationResponse forgot(ForgotPasswordRequest request){

        if(repository.findByEmail(request.getEmail()).isEmpty()){
            return AuthenticationResponse.builder()
                    .token("Invalid Email")
                    .build();
        }
        User user=new User();
        user=repository.findByEmail(request.getEmail()).orElseThrow();
        if(!user.getIsVerified()){
            return AuthenticationResponse.builder()
                    .token("User Not Verified")
                    .build();
        }

        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);

        String OTP=GenerateOtp.generateOtp();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(OTP);
        otpEntity.setEmail(request.getEmail());
        otpEntity.setExpirationTime(expirationTime);
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(),OTP);

        return AuthenticationResponse.builder()
                .token("Check your email for OTP")
                .build();

    }


    public AuthenticationResponse resetVerifyOtp(verifyResetPasswordRequest request){
        String OTP = otpService.getOtpByEmail(request.getEmail());
        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isAfter(otpUser.getExpirationTime())){
            return AuthenticationResponse.builder()
                    .token("OTP Expired")
                    .build();
        }
        if(request.getOtp().equals(OTP))
        {
            return AuthenticationResponse.builder()
                    .token("Otp Verified Successfully")
                    .build();
        }
        else {
            return AuthenticationResponse.builder()
                    .token("incorrect Otp")
                    .build();
        }
    }


    public AuthenticationResponse resetPassword(PasswordResetRequest request){
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        return AuthenticationResponse.builder()
                .token("Password has been reset Successfully")
                .build();


    }
    public AuthenticationResponse resend(ResendRequest request){


        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);
        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isBefore(otpUser.getExpirationTime().minusMinutes(9))){
            return AuthenticationResponse.builder()
                    .token("Please wait for 1 minute before sending another OTP")
                    .build();
        }


        String OTP=GenerateOtp.generateOtp();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(OTP);
        otpEntity.setEmail(request.getEmail());
        otpEntity.setExpirationTime(expirationTime);
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(),OTP);

        return AuthenticationResponse.builder()
                .token("OTP sent again")
                .build();
    }


}
