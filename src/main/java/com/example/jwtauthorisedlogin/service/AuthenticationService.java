package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Email.OtpEntity;
import com.example.jwtauthorisedlogin.payload.*;
import com.example.jwtauthorisedlogin.repository.OtpRepository;
import com.example.jwtauthorisedlogin.user.Role;
import com.example.jwtauthorisedlogin.user.User;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request, Role userRole) {

        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);

        var user=new User();

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
                return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthenticationResponse.builder().message("User Already Exists").build());
//                AuthenticationResponse.builder()
//                        .token("User already exists")
//                        .build();
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

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().message("Check your email for otp").build());
//                AuthenticationResponse.builder()
//                .token("Check your email for OTP")
//                .build();

    }


    public ResponseEntity<AuthenticationResponse> verify(VerifyRequest request){

        if (otpRepository.findByEmail(request.getEmail()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthenticationResponse.builder().message("No OTP genereated").build());
                    //AuthenticationResponse.builder().token("No OTP generated").build();
        }

        String OTP = otpService.getOtpByEmail(request.getEmail());

        if(!(request.getOtp().equals(OTP))){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder().message("Incorrect OTP").build());
                    //AuthenticationResponse.builder().token("Incorrect OTP").build();
        }

        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isAfter(otpUser.getExpirationTime())){
            return    ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthenticationResponse.builder().message("OTP expired").build());
//                    AuthenticationResponse.builder()
//                    .token("OTP Expired")
//                    .build();
        }

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        user.setIsVerified(true);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticationResponse.builder().token(jwtToken).build());
//                AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
    }


    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {

        var userOptional = repository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthenticationResponse.builder().message("User not Registered").build());
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
               return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder().message("Invalid Credentials").build());
           }

        if(!(user.getIsVerified())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthenticationResponse.builder().message("User Not Verified").build());
        }

            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(AuthenticationResponse.builder().token(jwtToken).name(user.getFullName()).hasRole(user.getRole()).build());
    }


    public ResponseEntity<AuthenticationResponse> forgot(ForgotPasswordRequest request){

        if(repository.findByEmail(request.getEmail()).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder().message("Invalid Email").build());

        }
        User user=new User();
        user=repository.findByEmail(request.getEmail()).orElseThrow();
        if(!user.getIsVerified()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthenticationResponse.builder().message("User not Verified").build());
//                    AuthenticationResponse.builder()
//                    .token("User Not Verified")
//                    .build();
        }

        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);

        String OTP=GenerateOtp.generateOtp();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(OTP);
        otpEntity.setEmail(request.getEmail());
        otpEntity.setExpirationTime(expirationTime);
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(),OTP);

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().message("Check your email for OTP").build());
//                AuthenticationResponse.builder()
//                .token("Check your email for OTP")
//                .build();

    }


    public ResponseEntity<AuthenticationResponse> resetVerifyOtp(verifyResetPasswordRequest request){
        String OTP = otpService.getOtpByEmail(request.getEmail());
        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isAfter(otpUser.getExpirationTime())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthenticationResponse.builder().message("OTP expired").build());
//                    AuthenticationResponse.builder()
//                    .token("OTP Expired")
//                    .build();
        }
        if(request.getOtp().equals(OTP))
        {
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().message("OTP Verified Successfully").build());
//                    AuthenticationResponse.builder()
//                    .token("Otp Verified Successfully")
//                    .build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder().message("Incorrect OTP").build());
//                    AuthenticationResponse.builder()
//                    .token("incorrect Otp")
//                    .build();
        }
    }


    public ResponseEntity<AuthenticationResponse> resetPassword(PasswordResetRequest request){
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().message("Password Has Been reset Successfully").build());
//                AuthenticationResponse.builder()
//                .token("Password has been reset Successfully")
//                .build();


    }
    public ResponseEntity<AuthenticationResponse> resend(ResendRequest request){


        LocalDateTime expirationTime=LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTE);
        var otpUser = otpRepository.findByEmail(request.getEmail()).orElseThrow();

        if(LocalDateTime.now().isBefore(otpUser.getExpirationTime().minusMinutes(9))){
            return ResponseEntity.status(HttpStatus.TOO_EARLY).body(AuthenticationResponse.builder().message("Please wait for 1 minute before sending another OTP").build());
//                    AuthenticationResponse.builder()
//                    .token("Please wait for 1 minute before sending another OTP")
//                    .build();
        }


        String OTP=GenerateOtp.generateOtp();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(OTP);
        otpEntity.setEmail(request.getEmail());
        otpEntity.setExpirationTime(expirationTime);
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(),OTP);

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().message("OTP sent again").build());
//                AuthenticationResponse.builder()
//                .token("OTP sent again")
//                .build();
    }


}
