package com.example.jwtauthorisedlogin.payload;

import com.example.jwtauthorisedlogin.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String message;
    private String name;
    private Role hasRole;
}


