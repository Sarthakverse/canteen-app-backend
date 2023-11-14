package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.ProfileUpdateRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.payload.response.UserProfileResponse;

import com.example.jwtauthorisedlogin.service.ProfileService;
import com.example.jwtauthorisedlogin.user.User;
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

    @GetMapping("/get")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        UserProfileResponse userProfile = profileService.getProfile();

        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

@PutMapping("/update")
    public ResponseEntity<MessageResponse> updateUserProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest){
        MessageResponse user = profileService.updateProfile(profileUpdateRequest);

        if (user.getMessage().contains("Profile has been updated")) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




}


