package com.example.jwtauthorisedlogin.Email;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(name = "OTP")
@Entity
public class OtpEntity {

    private String otp;
    @Id
    private String email;
    private LocalDateTime ExpirationTime;
}
