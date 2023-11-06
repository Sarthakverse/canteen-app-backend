package com.example.jwtauthorisedlogin.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanteenRequest {
    private String name;
    private String email;
    private String description;
    private String canteenImage;
}
