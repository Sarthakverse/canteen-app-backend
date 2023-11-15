package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Food;
//import com.example.jwtauthorisedlogin.payload.request.WishListGetRequest;
import com.example.jwtauthorisedlogin.payload.request.WishlistDeleteRequest;
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
        if(response.getMessage().contains("Food item added to the wishlist"))
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else if(response.getMessage().contains("Food item is already in the wishlist"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Food>> getWishlistItems(){
        List<Food> wishlistItems = wishlistService.getWishlistItems();
        return ResponseEntity.ok(wishlistItems);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> deleteWishlistItem(@RequestBody WishlistDeleteRequest request){
        MessageResponse response = wishlistService.deleteWishlistItem(request);
        if(response.getMessage().contains("Food item is not in the wishlist"))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }
}