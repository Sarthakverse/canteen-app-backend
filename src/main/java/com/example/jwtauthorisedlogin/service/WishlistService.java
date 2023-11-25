package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.Entity.UserWishlist;
//import com.example.jwtauthorisedlogin.payload.request.WishListGetRequest;
import com.example.jwtauthorisedlogin.payload.request.WishlistDeleteRequest;
import com.example.jwtauthorisedlogin.payload.request.WishlistRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.FoodRatingRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.repository.UserWishlistRepository;
import com.example.jwtauthorisedlogin.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistService {

    private final UserWishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final FoodRatingRepository foodRatingRepository;

    public MessageResponse addToWishlist(WishlistRequest request) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            var food = foodRepository.findById(request.getFoodId()).orElse(null);

            if(food == null){
                return MessageResponse.builder().message("Food item not found").build();
            }

            if (wishlistRepository.existsByUserAndFood(currentUser, food)) {
                return MessageResponse.builder().message("Food item is already in the wishlist").build();
            }


            var wishlistItem = UserWishlist.builder()
                .user(currentUser)
                .food(food)
                .build();

        wishlistRepository.save(wishlistItem);

        food.setIsInWishlist(true);
        foodRepository.save(food);

        return MessageResponse.builder().message("Food item added to the wishlist").build();
    }

    public List<Food> getWishlistItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);

        assert user != null;
        List<UserWishlist> wishlistItems = wishlistRepository.findByUserEmail(user.getEmail());
        List<Food> wishlistFoods = wishlistItems.stream()
                .map(UserWishlist::getFood)
                .toList();

        List<FoodRating> allRatings = foodRatingRepository.findAll();

        Set<Long> wishlistFoodItemIds = wishlistFoods.stream().map(Food::getId).collect(Collectors.toSet());

        Set<Long> cartFoodItemIds = user.getCarts().stream()
                .map(cart -> cart.getFoodId().getId())
                .collect(Collectors.toSet());


        Map<Long, Long> totalRatingsMap = allRatings.stream()
                .filter(rating -> rating.getFoodItem() != null)
                .filter(rating -> wishlistFoods.contains(rating.getFoodItem()))
                .collect(Collectors.groupingBy(
                        rating -> rating.getFoodItem().getId(),
                        Collectors.counting()
                ));

        for (Food food : wishlistFoods) {
            boolean isInCart = cartFoodItemIds.contains(food.getId());
            food.setIsInCart(isInCart);
            food.setNoOfRatings(totalRatingsMap.getOrDefault(food.getId(), 0L));
        }

        return wishlistFoods;
    }

    public MessageResponse deleteWishlistItem(WishlistDeleteRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        var food = foodRepository.findById(request.getFoodId()).orElse(null);

        if (Objects.equals(currentUser.getEmail(), request.getEmail())) {
            if (!wishlistRepository.existsByUserAndFood(user, food)) {
                return MessageResponse.builder().message("Food item is not in the wishlist").build();
            }

            var wishlistItem = wishlistRepository.findByUserAndFood(user, food).orElse(null);

            assert wishlistItem != null;
            wishlistRepository.delete(wishlistItem);
            assert food != null;
            food.setIsInWishlist(false);
            foodRepository.save(food);

            return MessageResponse.builder().message("Food item deleted from the wishlist").build();
        } else {
            return MessageResponse.builder().message("Food item is not in the wishlist").build();
        }
    }




}