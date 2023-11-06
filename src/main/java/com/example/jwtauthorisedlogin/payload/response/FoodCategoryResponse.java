package com.example.jwtauthorisedlogin.payload.response;

import com.example.jwtauthorisedlogin.Entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategoryResponse {
    private List<Food> foodItems;
}
