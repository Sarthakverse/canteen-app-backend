package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.payload.request.ProfileUpdateRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.payload.response.UserProfileResponse;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProfileService
{
    private final UserRepository userRepository;
    public UserProfileResponse getProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

           var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);
           if(user != null)
           {
               return UserProfileResponse.builder()
                       .fullName(user.getFullName())
                       .contactNumber(user.getPhoneNo())
                       .email(user.getEmail())
                       .profileImage(user.getProfileImage())
                       .build();
           }
           else{
               return null;
           }

    }

    public MessageResponse updateProfile(@Valid ProfileUpdateRequest profileUpdateRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);
        if(user != null)
        {
            user.setFullName(profileUpdateRequest.getFullName());
            user.setPhoneNo(profileUpdateRequest.getContactNumber());
            user.setProfileImage(profileUpdateRequest.getProfileImage());
            userRepository.save(user);
            return MessageResponse.builder().message("Profile has been updated").build();
        }
        else{
            return MessageResponse.builder().message("Profile has not been updated").build();
        }

    }




}
