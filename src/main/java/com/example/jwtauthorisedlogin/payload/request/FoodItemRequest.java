package com.example.jwtauthorisedlogin.payload.request;

import com.example.jwtauthorisedlogin.Entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemRequest {
    private String name;
    private Category category;
    private String description;
    private Double price;
    private String foodImage;;
    private Long canteenId;
    private Boolean veg;
    @NotEmpty
    @Size(min = 3, max = 3)
    private List<String> ingredients;

    @NotEmpty
    @Size(min = 3, max = 3)
    private List<String> ingredientImageList;
}
