package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.WishListGetRequest;
import com.example.jwtauthorisedlogin.payload.request.WishlistRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@AllArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addToWishlist(@RequestBody WishlistRequest request){
        MessageResponse response = wishlistService.addToWishlist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Food>> getWishlistItems(@RequestBody WishListGetRequest request){
        List<Food> wishlistItems = wishlistService.getWishlistItems(request);
        return ResponseEntity.ok(wishlistItems);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> deleteWishlistItem(@RequestBody WishlistRequest request){
        MessageResponse response = wishlistService.deleteWishlistItem(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}