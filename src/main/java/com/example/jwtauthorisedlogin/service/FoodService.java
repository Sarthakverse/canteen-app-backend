package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.request.FoodCategoryRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodDetailsRequest;
import com.example.jwtauthorisedlogin.payload.request.FoodItemRequest;
import com.example.jwtauthorisedlogin.payload.response.FoodCategoryResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.CanteenRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    //@Value("${project.image}")
    private  String path=System.getProperty("user.dir");

    private final FoodRepository foodRepository;
    private final CanteenRepository canteenRepository;

    public MessageResponse createFoodItem(FoodItemRequest request) throws IOException {
        var food = new Food();
        food.setName(request.getName());
        food.setCategory(request.getCategory());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setCanteenId(request.getCanteenId());
        food.setIngredients(request.getIngredients());
        food.setIngredientImageList(request.getIngredientImageList());

        Food existingFood = foodRepository.findByNameAndCanteenId(request.getName(), request.getCanteenId());

        if (request.getFoodImage() != null) {
            MultipartFile foodImageFile = request.getFoodImage();

            String uniqueFileName = UUID.randomUUID().toString() + "_" + foodImageFile.getOriginalFilename();
            String imagePath = path + "/images/food/" + uniqueFileName;

            if (existingFood != null) {
                //if (existingFood.getFoodImageName() != null) {
                    File oldImage = new File(path + "/images/food/" + existingFood.getFoodImageName());
                    if (oldImage.exists()) {
                        FileUtils.forceDelete(oldImage);
                    }
                //}
                existingFood.setFoodImageName(uniqueFileName);
            }

            foodImageFile.transferTo(new File(imagePath));
        }

        if (existingFood != null) {
            existingFood.setCategory(request.getCategory());
            existingFood.setDescription(request.getDescription());
            existingFood.setPrice(request.getPrice());
            existingFood.setFoodImage(request.getFoodImage());
            existingFood.setIngredients(request.getIngredients());
            existingFood.setIngredientImageList(request.getIngredientImageList());
            foodRepository.save(existingFood);

            return MessageResponse.builder().message(request.getName() + " was updated").build();
        } else {
            var canteen = canteenRepository.findById(request.getCanteenId()).orElseThrow();
            canteen.getFoods().add(food);
            foodRepository.save(food);
            canteenRepository.save(canteen);
            return MessageResponse.builder().message(request.getName() + " was added").build();
        }

    }

    public FoodCategoryResponse getFoodItem(FoodCategoryRequest request){
        List<Food> foodList = foodRepository.findByCategory(request.getCategory());
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }

    public FoodCategoryResponse getFoodDetails(FoodDetailsRequest request){
        List<Food> foodList=foodRepository.findByNameContaining(request.getName());
        return FoodCategoryResponse.builder()
                .foodItems(foodList)
                .build();
    }
    public Optional<Food> getFoodById(Long id){
        return foodRepository.findById(id);
    }

}
