package com.example.jwtauthorisedlogin.payload;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class verifyResetPasswordRequest {
    private String email;
    private String otp;
}
