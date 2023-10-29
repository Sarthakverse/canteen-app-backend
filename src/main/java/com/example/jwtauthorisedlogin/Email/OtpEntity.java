package com.example.jwtauthorisedlogin.Email;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Table(name = "OTP")
@Entity
public class OtpEntity {

    private String otp;
    @Id
    private String email;
    private LocalDateTime ExpirationTime;
}
