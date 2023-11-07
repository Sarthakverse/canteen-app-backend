package com.example.jwtauthorisedlogin.payload.request;

import com.example.jwtauthorisedlogin.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemRequest {
    private String name;
    private Category category;
    private String description;
    private Double price;
    private String foodImage;
    private Long canteenId;
}
