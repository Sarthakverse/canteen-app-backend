package com.example.jwtauthorisedlogin.controller;


import com.example.jwtauthorisedlogin.payload.request.GetProfileRequest;
import com.example.jwtauthorisedlogin.payload.request.ProfileUpdateRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.payload.response.UserProfileResponse;
import com.example.jwtauthorisedlogin.service.ProfileService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @PutMapping("/update-profile")
    public ResponseEntity<MessageResponse> updateProfile(@Valid  @RequestBody ProfileUpdateRequest profileUpdateRequest){
        try {
            MessageResponse response = profileService.updateProfile(profileUpdateRequest);
            if (response.getMessage().contains("could not fetch data")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message("Profile Not Updated").build());
        }
    }

    @GetMapping("/get-profile")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestBody GetProfileRequest request)
    {
        try {
            UserProfileResponse userProfile = profileService.getProfile(request.getEmail());
            if (userProfile != null) {
                return ResponseEntity.status(HttpStatus.OK).body(userProfile);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}


