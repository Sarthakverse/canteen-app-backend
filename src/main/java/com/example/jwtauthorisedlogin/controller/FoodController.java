package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.response.CanteenDetailsResponse;
import com.example.jwtauthorisedlogin.payload.response.CanteenFoodResponse;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.service.CanteenService;
import com.example.jwtauthorisedlogin.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final CanteenService canteenService;

    @PostMapping("/get-food-items")
    public ResponseEntity<FoodCategoryResponse> getFoodItems(@RequestBody FoodCategoryRequest request)
    {
        try {
            var foodItems = foodService.getFoodItem(request);
            if(foodItems!=null)
                return ResponseEntity.ok(foodItems);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/get-food-details")
    public ResponseEntity<FoodCategoryResponse> getFoodDetails(
            @RequestBody FoodDetailsRequest request
            ){
        try {
            return ResponseEntity.ok(foodService.getFoodDetails(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/get-canteen-food")
    public ResponseEntity<CanteenFoodResponse> getCanteenFood(
            @RequestBody FoodDetailsRequest request
    ){
        try {
            return ResponseEntity.ok(canteenService.canteenFood(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/get-canteens")
    public ResponseEntity<CanteenDetailsResponse> getCanteenDetails(){
        try {
            return ResponseEntity.ok(canteenService.canteenDetails());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/food/{foodId}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long foodId) {
        Optional<Food> food = foodService.getFoodById(foodId);
        return food.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/canteen/{canteenId}")
    public ResponseEntity<Canteen> getCanteenById(@PathVariable Long canteenId) {
        Optional<Canteen> canteen = canteenService.getCanteenById(canteenId);
        return canteen.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Food>> searchFoods(
            @RequestParam(value = "canteenId", required = false, defaultValue = "") Long canteenId,
            @RequestParam(value="foodName", required= false, defaultValue = "") String foodName,
            @RequestParam(value="category", required= false, defaultValue = "") String category,
            @RequestParam(value="lowPrice", required= false, defaultValue = "0.0") Double lowPrice,
            @RequestParam(value="highPrice", required= false, defaultValue = "1.7976931348623157E308    ") Double highPrice,
            @RequestParam(value="veg", required= false, defaultValue = "") Boolean veg,
            @RequestParam(value="rating", required= false, defaultValue = "0.0") Double rating
    ) {

        Category categoryEnum = (category.isBlank()) ? null : Category.valueOf(category);

        try {
            return ResponseEntity.ok(foodService.search(canteenId,foodName,categoryEnum,lowPrice,highPrice,veg,rating));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/check-roles")
    public String checkRoles(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }

}
