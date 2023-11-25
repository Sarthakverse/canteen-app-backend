package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.CartDiscountRequest;
import com.example.jwtauthorisedlogin.payload.request.CartItemDeleteRequest;
import com.example.jwtauthorisedlogin.payload.request.CartRequest;
import com.example.jwtauthorisedlogin.payload.response.DiscountedPriceResponse;
import com.example.jwtauthorisedlogin.payload.response.GetCartItemResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CartRepository;
import com.example.jwtauthorisedlogin.repository.FoodRatingRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.user.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public Cart addToCart(CartRequest cartRequest) {
        var selectedFood = foodRepository.findById(cartRequest.getFoodId()).orElse(null);

        if (selectedFood != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            Cart existingCartItem = cartRepository.findByFoodIdAndUser(selectedFood, currentUser).orElse(null);
            if (existingCartItem != null) {
                existingCartItem.setQuantity(cartRequest.getQuantity());
                existingCartItem.setPrice(selectedFood.getPrice() * cartRequest.getQuantity());
                selectedFood.setIsInCart(true);
                foodRepository.save(selectedFood);
                return cartRepository.save(existingCartItem);
            }

                Double price = selectedFood.getPrice() * cartRequest.getQuantity();

                Cart cartEntry = new Cart();
                cartEntry.setFoodItemName(selectedFood.getName());
                cartEntry.setQuantity(cartRequest.getQuantity());
                cartEntry.setPrice(price);
                cartEntry.setUser(currentUser);
                cartEntry.setFoodId(selectedFood);
                selectedFood.setIsInCart(true);
                foodRepository.save(selectedFood);
                return cartRepository.save(cartEntry);
            }
        return null;
    }


    @Transactional
    public ResponseEntity<MessageResponse> deleteCartItem(CartItemDeleteRequest cartItemDeleteRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Cart cartItem = cartRepository.findByFoodIdIdAndUserEmail(cartItemDeleteRequest.getFoodItemId(), currentUser.getEmail()).orElse(null);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.builder().message("Item not found in the cart").build());
        }

        cartRepository.deleteById(cartItem.getId());

        Food food = cartItem.getFoodId();
        food.setIsInCart(false);
        foodRepository.save(food);

        return ResponseEntity.ok(MessageResponse.builder().message("Item deleted from the cart").build());
    }

    public List<GetCartItemResponse> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return cartRepository.findCartResponseByUserEmail(currentUser.getEmail());
    }

    @Transactional
    public Double getTotalCartPriceByUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Cart> cartItems = cartRepository.findByUserEmail(currentUser.getEmail());
        Double total = 0.0;

        for (Cart cartItem : cartItems) {
            total += cartItem.getPrice();
        }

        return total;
    }

    @Transactional
    public DiscountedPriceResponse getDiscountedPriceByUser(CartDiscountRequest cartDiscountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Cart> cartItems = cartRepository.findByUserEmail(currentUser.getEmail());
        Double total = 0.0;
        var discountCode = cartDiscountRequest.getCouponCode();

        for (Cart cartItem : cartItems) {
            System.out.print(cartItem+" ");
            total += cartItem.getPrice();
        }

        if ("DISCOUNT10".equals(discountCode)) {
            total = total - (total * 0.1);
            return new DiscountedPriceResponse(total);
        } else {
            return new DiscountedPriceResponse("Invalid coupon code: " + discountCode);
        }
    }

}