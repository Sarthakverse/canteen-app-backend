package com.example.jwtauthorisedlogin.service;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodRatingRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.FoodRatingRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class FoodRatingService
{
       private final FoodRepository foodRepository;
       private final FoodRatingRepository foodRatingRepository;
       private final UserRepository userRepository;

       public MessageResponse rateFoodItem(FoodRatingRequest foodRatingRequest)
       {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           User currentUser = (User) authentication.getPrincipal();
            var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);

           if (user != null) {
               var foodItem = foodRepository.findById(foodRatingRequest.getFoodItemId()).orElse(null);

               if (foodItem != null) {
                   FoodRating existingRating = foodRatingRepository.findByUserAndFoodItem(user, foodItem).orElse(null);

                   if (existingRating != null) {
                       existingRating.setRating(foodRatingRequest.getRating());
                       foodRatingRepository.save(existingRating);
                       double averageRating = getAvgRating(foodItem.getId());
                       foodItem.setAverageRating(averageRating);
                       foodRepository.save(foodItem);
                       return new MessageResponse("Food rating updated successfully!");
                   } else {
                       FoodRating newRating = new FoodRating();
                       newRating.setUser(user);
                       newRating.setFoodItem(foodItem);
                       newRating.setRating(foodRatingRequest.getRating());
                       foodRatingRepository.save(newRating);
                       double averageRating = getAvgRating(foodItem.getId());
                       foodItem.setAverageRating(averageRating);
                       foodRepository.save(foodItem);
                       return new MessageResponse("Food rated successfully!");
                   }
               } else {
                   return new MessageResponse("Food item not found");
               }
           } else {
               return new MessageResponse("User not found");
           }
       }

    public double getAvgRating(Long foodItemId) {
         return foodRatingRepository
                 .findAll()
                 .stream()
                 .filter(rating -> rating.getFoodItem() != null && rating.getFoodItem().getId().equals(foodItemId))
                 .mapToDouble(FoodRating::getRating)
                 .average()
                 .orElse(0.0);
    }

}
