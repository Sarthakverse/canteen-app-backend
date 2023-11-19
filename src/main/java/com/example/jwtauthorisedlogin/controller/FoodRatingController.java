package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.FoodRatingRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.FoodRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class FoodRatingController {
    private final FoodRatingService foodRatingService;

    @PostMapping("/rate")
    public ResponseEntity<MessageResponse> rateFoodItem(@Valid @RequestBody FoodRatingRequest foodRatingRequest)
    {
        try {
            MessageResponse response = foodRatingService.rateFoodItem(foodRatingRequest);
            if(response.getMessage().contains("Food rating updated successfully!") || response.getMessage().contains("Food rated successfully!") ) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else if(response.getMessage().contains("Food item not found")){
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Error rating food item"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/average-rating/{foodItemId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long foodItemId) {
        try {
            double averageRating = foodRatingService.getAvgRating(foodItemId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0.0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
