package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.CanteenService;
import com.example.jwtauthorisedlogin.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
