package com.example.jwtauthorisedlogin.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountedPriceResponse {
    private Double discountedPrice;
    private String errorMessage;
    public DiscountedPriceResponse(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    public DiscountedPriceResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
