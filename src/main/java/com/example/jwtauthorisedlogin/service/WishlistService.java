package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.UserWishlist;
import com.example.jwtauthorisedlogin.payload.request.WishListGetRequest;
import com.example.jwtauthorisedlogin.payload.request.WishlistRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.repository.UserWishlistRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistService {

    private final UserWishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public MessageResponse addToWishlist(WishlistRequest request) {
            var user = userRepository.findByEmail(request.getUserEmail()).orElse(null);
            var food = foodRepository.findById(request.getFoodId()).orElse(null);

            if (wishlistRepository.existsByUserAndFood(user, food)) {
                return MessageResponse.builder().message("Food item is already in the wishlist").build();
            }


            var wishlistItem = UserWishlist.builder()
                .user(user)
                .food(food)
                .build();

        wishlistRepository.save(wishlistItem);

        return MessageResponse.builder().message("Food item added to the wishlist").build();
    }

    public List<Food> getWishlistItems(WishListGetRequest request) {
        return wishlistRepository.findByUserEmail(request.getUserEmail())
                .stream()
                .map(UserWishlist::getFood)
                .collect(Collectors.toList());
    }

    public MessageResponse deleteWishlistItem(WishlistRequest request) {
        var user = userRepository.findByEmail(request.getUserEmail()).orElse(null);
        var food = foodRepository.findById(request.getFoodId()).orElse(null);

        if (!wishlistRepository.existsByUserAndFood(user, food)) {
            return MessageResponse.builder().message("Food item is not in the wishlist").build();
        }

        var wishlistItem = wishlistRepository.findByUserAndFood(user, food).orElse(null);

        wishlistRepository.delete(wishlistItem);

        return MessageResponse.builder().message("Food item deleted from the wishlist").build();
    }

}