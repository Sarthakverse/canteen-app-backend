package com.example.jwtauthorisedlogin.controller;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final FoodService foodService;
    private final CanteenService canteenService;

    @PostMapping("/get-food-items")
    public ResponseEntity<FoodCategoryResponse> getFoodItems(
            @RequestBody FoodCategoryRequest request
            ){
        try {
            return ResponseEntity.ok(foodService.getFoodItem(request));
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
}
