package com.example.jwtauthorisedlogin.payload.request;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodRatingRequest {
    private Long foodItemId;
    @Max(value = 5)
    private int rating;
}
