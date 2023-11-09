package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.CanteenRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.CanteenService;
import com.example.jwtauthorisedlogin.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final FoodService foodService;
    private final CanteenService canteenService;

    @PostMapping("/create-food-item")
    public ResponseEntity<MessageResponse> createFoodItem( @RequestBody FoodItemRequest request){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFoodItem(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message("Food item not added").build());
        }
    }

    @PostMapping("/create-canteen")
    public ResponseEntity<MessageResponse> createCanteen(@RequestBody CanteenRequest request){

        try {
            MessageResponse response = canteenService.createCanteen(request);
            if (response.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message("Canteen not added").build());
        }
    }


}
