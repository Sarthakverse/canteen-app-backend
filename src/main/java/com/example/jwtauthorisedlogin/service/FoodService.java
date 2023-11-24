package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;
    private final CanteenRepository canteenRepository;
    private final FoodRatingRepository foodRatingRepository;
    private final UserRepository userRepository;
    private final UserWishlistRepository userWishlistRepository;
    private final CartRepository cartRepository;
    public MessageResponse createFoodItem(FoodItemRequest request) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        var user = userRepository.findByEmail(userEmail).orElse(null);
        if(user !=null) {
            var userWishlist = userWishlistRepository.findByUserEmail(userEmail);
            var cart = cartRepository.findByUserEmail(userEmail);
            List<Food> foodList = foodRepository.findByCategory(request.getCategory());
            List<FoodRating> allRatings = foodRatingRepository.findAll();


            Map<String, Double> averageRatingsMap = allRatings.stream()
                    .filter(rating -> rating.getFoodItem() != null)
                    .collect(Collectors.groupingBy(
                            rating -> rating.getFoodItem().getId() + "-" + rating.getFoodItem().getCanteenId(),
                            Collectors.averagingDouble(FoodRating::getRating)
                    ));
            List<Food> updatedFoods = new ArrayList<>();
            for (Food food : foodList) {
                String key = food.getId() + "-" + food.getCanteenId();
                double avgRating = averageRatingsMap.getOrDefault(key, 0.0);
                food.setAverageRating(avgRating);

                boolean isInWishlist = userWishlist.stream().anyMatch(wishlistItem -> wishlistItem.getFood().getId().equals(food.getId()));
                food.setIsInWishlist(isInWishlist);

                boolean isInCart = cart.stream().anyMatch(cartItem -> cartItem.getFoodId().getId().equals(food.getId()));
                food.setIsInCart(isInCart);
                updatedFoods.add(food);
            }
            foodRepository.saveAll(updatedFoods);
            return FoodCategoryResponse.builder()
                    .foodItems(foodList)
                    .build();

        }
        else{
            return null;
        }
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
