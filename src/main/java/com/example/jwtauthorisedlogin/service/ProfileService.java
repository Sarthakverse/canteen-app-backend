package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.ProfileUpdateRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.payload.response.UserProfileResponse;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService
{
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public MessageResponse updateProfile(ProfileUpdateRequest profileUpdateRequest)
    {
        User existingUser = userRepository.findByEmail(profileUpdateRequest.getEmail()).orElse(null);
        if(existingUser!=null){

            existingUser.setFullName(profileUpdateRequest.getYourName());
            existingUser.setEmail(profileUpdateRequest.getEmail());
            existingUser.setContactNumber(profileUpdateRequest.getContactNumber());
            existingUser.setDateOfBirth(profileUpdateRequest.getDateOfBirth());
            existingUser.setGender(profileUpdateRequest.getGender());


        userRepository.save(existingUser);

        return MessageResponse.builder().message("Profile has been updated").build();
        }
        else{
            return MessageResponse.builder().message("could not fetch data").build();
        }
    }

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return UserProfileResponse.builder()
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .contactNumber(user.getContactNumber())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender().toString())
                    .build();
        } else {
            return null;
        }
    }

}
