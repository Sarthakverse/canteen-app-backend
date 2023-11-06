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
public class FoodCategoryRequest {
    private Category category;
}
