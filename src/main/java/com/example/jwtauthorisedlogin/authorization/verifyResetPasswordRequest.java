package com.example.jwtauthorisedlogin.authorization;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class verifyResetPasswordRequest {
    private String email;
    private String otp;
}
