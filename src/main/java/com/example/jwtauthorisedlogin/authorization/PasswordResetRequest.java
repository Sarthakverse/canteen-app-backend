package com.example.jwtauthorisedlogin.authorization;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    private String email;
    private String newPassword;
}
