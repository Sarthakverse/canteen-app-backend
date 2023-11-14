package com.example.jwtauthorisedlogin.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDeleteRequest {
    private String email;
    private Long foodId;
}
