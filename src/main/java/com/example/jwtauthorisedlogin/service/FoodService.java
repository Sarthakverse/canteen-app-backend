package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.AuthenticationResponse;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CanteenRepository canteenRepository;

    public MessageResponse createFoodItem(FoodItemRequest request){
        var food = new Food();
        food.setName(request.getName());
        food.setCategory(request.getCategory());
        food.setDescription(request.getDescription());
        food.setPrice((request.getPrice()));
        food.setFoodImage(request.getFoodImage());

        foodRepository.save(food);

        return MessageResponse.builder().message(request.getName()+" was added").build();

    }

    public FoodCategoryResponse getFoodItem(FoodCategoryRequest request){
        List<Food> foodList = foodRepository.findByCategory(request.getCategory());
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }

}
