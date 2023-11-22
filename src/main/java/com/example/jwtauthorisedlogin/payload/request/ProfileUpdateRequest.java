package com.example.jwtauthorisedlogin.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String profileImage;
    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @Pattern(regexp = "^(0|([1-9]\\d{9}))$")
    private String contactNumber;


}
