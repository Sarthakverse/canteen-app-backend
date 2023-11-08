package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.CanteenRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.response.CanteenDetailsResponse;
import com.example.jwtauthorisedlogin.payload.response.CanteenFoodResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenFoodRepository;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
    private final FoodRepository foodRepository;
    private final CanteenFoodRepository canteenFoodRepository;

    public MessageResponse createCanteen(CanteenRequest request){
        var canteen=new Canteen();
        canteen.setName(request.getName());
        canteen.setEmail(request.getEmail());
        canteen.setDescription(request.getDescription());
        canteen.setCanteenImage(request.getCanteenImage());
        canteenRepository.save(canteen);

        return MessageResponse.builder().message(request.getName()+" was added").build();
    }

    public CanteenFoodResponse canteenFood(FoodDetailsRequest request){
        var canteen= canteenRepository.findByName(request.getName());
        Set<Long> foodItemsId=canteenFoodRepository.findFoodIdsByCanteenId(canteen.getId());
        Set<Food> foodItems = new HashSet<>();

        for (Long foodId : foodItemsId) {
            Food food = foodRepository.findById(foodId).orElse(null);
            if (food != null) {
                foodItems.add(food);
            }
        }

        return CanteenFoodResponse.builder().foodItems(foodItems).build();
    }
    public CanteenDetailsResponse canteenDetails(){
        List<Canteen> canteenList = canteenRepository.findAll();
        return new CanteenDetailsResponse(canteenList);
    }

    public Optional<Canteen> getCanteenById(Long id){
        return canteenRepository.findById(id);
    }

}
