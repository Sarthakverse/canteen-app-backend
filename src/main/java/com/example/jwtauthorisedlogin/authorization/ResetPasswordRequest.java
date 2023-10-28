package com.example.jwtauthorisedlogin.authorization;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String email;
    private String NewPassword;
    private String ConfirmPassword;
}
