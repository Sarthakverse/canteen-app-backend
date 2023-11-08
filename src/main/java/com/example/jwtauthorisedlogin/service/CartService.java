package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.CartRequest;
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
    private final CanteenRepository canteenRepository;
    @Transactional
    public Cart addToCart(CartRequest cartRequest) {
        var selectedFood = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
        var selectedCanteen = canteenRepository.findById(cartRequest.getCanteenId()).orElse(null);


        if(selectedFood != null && selectedCanteen != null) {
            Food foodInCanteen = selectedCanteen.getFoods().stream()
                    .filter(food -> food.getId().equals(selectedFood.getId()))
                    .findFirst()
                    .orElse(null);

            if (foodInCanteen != null) {
                Double price = selectedFood.getPrice() * cartRequest.getQuantity();

                Cart cartEntry = new Cart();
                cartEntry.setFoodItemName(selectedFood.getName());
                cartEntry.setQuantity(cartRequest.getQuantity());
                cartEntry.setPrice(price);

                return cartRepository.save(cartEntry);
            }
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