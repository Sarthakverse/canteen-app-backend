package com.example.jwtauthorisedlogin.payload;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    private String email;
    private String newPassword;
}
