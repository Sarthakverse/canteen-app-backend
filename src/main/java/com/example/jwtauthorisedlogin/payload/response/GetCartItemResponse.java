package com.example.jwtauthorisedlogin.payload.response;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCartItemResponse {
    private Long id;
    private String foodItemName;
    private Long foodItemId;
    private Integer quantity;
    private Double price;
    private Long canteenId;
    private Double averageRating;
}
