package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.Entity.UserWishlist;
import com.example.jwtauthorisedlogin.payload.request.CanteenRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.response.CanteenDetailsResponse;
import com.example.jwtauthorisedlogin.payload.response.CanteenFoodResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.*;
import com.example.jwtauthorisedlogin.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
    private final FoodRepository foodRepository;
    private final CanteenFoodRepository canteenFoodRepository;
    private final FoodRatingRepository foodRatingRepository;

    public MessageResponse createCanteen(CanteenRequest request){
        if (canteenRepository.findByEmail(request.getEmail())!=null) {
            return MessageResponse.builder().message("Canteen with email " + request.getEmail() + " already exists").build();
        }
        var canteen=new Canteen();
        canteen.setName(request.getName());
        canteen.setEmail(request.getEmail());
        canteen.setDescription(request.getDescription());
        canteen.setCanteenImage(request.getCanteenImage());

        canteenRepository.save(canteen);

        return MessageResponse.builder().message(request.getName()+" was added").build();
    }
    public CanteenFoodResponse canteenFood(FoodDetailsRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        var canteen = canteenRepository.findByName(request.getName());
        Set<Long> foodItemsId = canteenFoodRepository.findFoodIdsByCanteenId(canteen.getId());
        Set<Food> foodItems = new HashSet<>();

        Set<Long> cartFoodItemIds = currentUser.getCarts().stream()
                .map(cart -> cart.getFoodId().getId())
                .collect(Collectors.toSet());

        Set<Long> wishlistFoodItemIds = currentUser.getWishlist().stream()
                .map(wishlist -> wishlist.getFood().getId())
                .collect(Collectors.toSet());

        for (Long foodId : foodItemsId) {
            Optional<Food> optionalFood = foodRepository.findById(foodId);
            optionalFood.ifPresent(food -> {

                boolean isInCart = cartFoodItemIds.contains(food.getId());


                boolean isInWishlist = wishlistFoodItemIds.contains(food.getId());
                // Fetch ratings for the current food item
                List<FoodRating> ratingsForFood = foodRatingRepository.findByFoodItem(food);

                long totalRatings = ratingsForFood.stream().count();

                food.setNoOfRatings(totalRatings);

                food.setIsInCart(isInCart);
                food.setIsInWishlist(isInWishlist);

                foodItems.add(food);
            });
        }
        return CanteenFoodResponse.builder().foodItems(foodItems).build();
    }
    public CanteenDetailsResponse canteenDetails(){
        List<Canteen> canteenList = canteenRepository.findAll();
        return new CanteenDetailsResponse(canteenList);
    }

    public Optional<Canteen> getCanteenById(Long id){
        return canteenRepository.findById(id);
    }

}
