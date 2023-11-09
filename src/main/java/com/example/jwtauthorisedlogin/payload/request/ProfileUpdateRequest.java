package com.example.jwtauthorisedlogin.payload.request;

import com.example.jwtauthorisedlogin.user.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    @NotBlank
    private String yourName;
    @Size(max = 10, message = "Contact number must be at most 10 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Contact number must contain only digits")
    private String contactNumber;
    @NotBlank
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;
}
