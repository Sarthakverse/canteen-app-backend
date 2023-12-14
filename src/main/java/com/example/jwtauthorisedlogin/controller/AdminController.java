package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.CanteenRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.payload.response.OrderHistoryResponse;
import com.example.jwtauthorisedlogin.service.CanteenService;
import com.example.jwtauthorisedlogin.service.FoodService;
import com.example.jwtauthorisedlogin.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final FoodService foodService;
    private final CanteenService canteenService;
    private final PaymentService paymentService;

    @PostMapping("/create-food-item")
    public ResponseEntity<MessageResponse> createFoodItem(@Valid @RequestBody FoodItemRequest request){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFoodItem(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message("Food item not added").build());
        }
    }

    @DeleteMapping("/delete/{foodItemId}")
    public ResponseEntity<MessageResponse> deleteFoodItem(@PathVariable Long foodItemId) {
        MessageResponse response = foodService.deleteFoodItemById(foodItemId);
        return ResponseEntity.ok(response);
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

    @GetMapping("/available/{canteenId}")
    public ResponseEntity<List<Food>> available(@PathVariable Long canteenId){
        try {
            return ResponseEntity.ok(foodService.available(canteenId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/order-history/{canteenId}")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistoryByCanteenId(@PathVariable Long canteenId) {
        try{
            return ResponseEntity.ok(paymentService.getOrderHistoryByCanteenId(canteenId));
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

}
