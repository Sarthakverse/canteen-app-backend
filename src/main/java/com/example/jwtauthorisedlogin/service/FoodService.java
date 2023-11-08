package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CanteenRepository canteenRepository;

    public MessageResponse createFoodItem(FoodItemRequest request) throws IOException {
        // create constructors for this
        var food = new Food();
        food.setName(request.getName());
        food.setCategory(request.getCategory());
        food.setDescription(request.getDescription());
        food.setPrice((request.getPrice()));
        food.setCanteenId(request.getCanteenId());
        food.setFoodImage(request.getFoodImage());

        Food existingFood = foodRepository.findByNameAndCanteenId(request.getName(), request.getCanteenId());

        if (existingFood != null) {
            existingFood.setCategory(request.getCategory());
            existingFood.setDescription(request.getDescription());
            existingFood.setPrice(request.getPrice());
            existingFood.setFoodImage(request.getFoodImage());
            foodRepository.save(existingFood);
        }
        else {
            var canteen = canteenRepository.findById(request.getCanteenId()).orElseThrow();
            canteen.getFoods().add(food);
            foodRepository.save(food);
            canteenRepository.save(canteen);
        }

        return MessageResponse.builder().message(request.getName()+" was added").build();

    }

    public FoodCategoryResponse getFoodItem(FoodCategoryRequest request){
        List<Food> foodList = foodRepository.findByCategory(request.getCategory());
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }

    public FoodCategoryResponse getFoodDetails(FoodDetailsRequest request){
        List<Food> foodList=foodRepository.findByNameContaining(request.getName());
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }
    public Optional<Food> getFoodById(Long id){
        return foodRepository.findById(id);
    }

}
