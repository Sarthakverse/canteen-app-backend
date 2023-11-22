package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.FoodRatingRepository;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CanteenRepository canteenRepository;
    private final FoodRatingRepository foodRatingRepository;
    public MessageResponse createFoodItem(FoodItemRequest request) throws IOException {
        var food = new Food();
        food.setName(request.getName());
        food.setCategory(request.getCategory());
        food.setDescription(request.getDescription());
        food.setPrice((request.getPrice()));
        food.setCanteenId(request.getCanteenId());
        food.setFoodImage(request.getFoodImage());
        food.setIngredients(request.getIngredients());
        food.setIngredientImageList(request.getIngredientImageList());
        Food existingFood = foodRepository.findByNameAndCanteenId(request.getName(), request.getCanteenId());

        if (existingFood != null) {
            existingFood.setCategory(request.getCategory());
            existingFood.setDescription(request.getDescription());
            existingFood.setPrice(request.getPrice());
            existingFood.setFoodImage(request.getFoodImage());
            existingFood.setIngredients(request.getIngredients());
            existingFood.setIngredientImageList(request.getIngredientImageList());
            foodRepository.save(existingFood);

            return MessageResponse.builder().message(request.getName()+" was updated").build();
        }
        else {
            var canteen = canteenRepository.findById(request.getCanteenId()).orElseThrow();
            canteen.getFoods().add(food);
            foodRepository.save(food);
            canteenRepository.save(canteen);
            return MessageResponse.builder().message(request.getName()+" was added").build();
        }



    }

    public FoodCategoryResponse getFoodItem(FoodCategoryRequest request){
        List<Food> foodList = foodRepository.findByCategory(request.getCategory());
        List<FoodRating> allRatings = foodRatingRepository.findAll();

        Map<String, Double> averageRatingsMap = allRatings.stream()
                .filter(rating -> rating.getFoodItem() != null)
                .collect(Collectors.groupingBy(
                        rating -> rating.getFoodItem().getId() + "-" + rating.getFoodItem().getCanteenId(),
                        Collectors.averagingDouble(FoodRating::getRating)
                ));

        for (Food food : foodList) {
            String key = food.getId() + "-" + food.getCanteenId();
            double avgRating = averageRatingsMap.getOrDefault(key, 0.0);
            food.setAverageRating(avgRating);
            foodRepository.save(food);
        }
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }

    public FoodCategoryResponse getFoodDetails(FoodDetailsRequest request){

        List<Food> foodList=foodRepository.findByNameContaining(request.getName());


        List<FoodRating> allRatings = foodRatingRepository.findAll();

        Map<Long, Double> averageRatingsMap = allRatings.stream()
                .filter(rating -> rating.getFoodItem() != null)
                .collect(Collectors.groupingBy(
                        rating -> rating.getFoodItem().getId(),
                        Collectors.averagingDouble(FoodRating::getRating)
                ));

        for (Food food : foodList) {
            long foodItemId = food.getId();
            double avgRating = averageRatingsMap.getOrDefault(foodItemId, 0.0);
            food.setAverageRating(avgRating);
        }

        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }
    public Optional<Food> getFoodById(Long id){
        return foodRepository.findById(id);
    }

}
