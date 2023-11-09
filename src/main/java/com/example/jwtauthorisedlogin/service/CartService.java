package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.CartRequest;
import com.example.jwtauthorisedlogin.repository.CanteenFoodRepository;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.CartRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;
    private final CanteenFoodRepository canteenFoodRepository;
    @Transactional
    public Cart addToCart(CartRequest cartRequest) {
        var selectedFood = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
//      var selectedCanteen = canteenFoodRepository.findFoodIdsByCanteenId(cartRequest.getCanteenId());

        if (selectedFood != null){

                Double price = selectedFood.getPrice() * cartRequest.getQuantity();

                Cart cartEntry = new Cart();
                cartEntry.setFoodItemName(selectedFood.getName());
                cartEntry.setQuantity(cartRequest.getQuantity());
                cartEntry.setPrice(price);

                return cartRepository.save(cartEntry);
            }
        return null;
    }


    public List<Cart> getCartItems() {
        return cartRepository.findAll();
    }

    public Double getTotalCartPrice() {
        List<Cart> cartItems = getCartItems();
        Double total = 0.0;

        for (Cart cartItem : cartItems) {
            total += cartItem.getPrice();
        }

        return total;
    }
}