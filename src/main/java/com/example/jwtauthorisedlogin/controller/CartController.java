package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.payload.request.CartRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<MessageResponse> addToCart(@RequestBody CartRequest cartRequest) {
        Cart cartEntry = cartService.addToCart(cartRequest);
        if (cartEntry != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MessageResponse.builder().message("Item added to the cart").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.builder().message("Food item not found").build());
        }
    }

    @GetMapping("/get-cart-items")
    public ResponseEntity<List<Cart>> getCartItems() {
        List<Cart> cartItems = cartService.getCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/get-total-cart-price")
    public ResponseEntity<Double> getTotalCartPrice() {
        Double totalCartPrice = cartService.getTotalCartPrice();
        return ResponseEntity.ok(totalCartPrice);
    }

    @DeleteMapping("/delete-cart-item/{id}")
    public ResponseEntity<MessageResponse> deleteCartItem(@PathVariable Long id) {
        return cartService.deleteCartItem(id);
    }
}
