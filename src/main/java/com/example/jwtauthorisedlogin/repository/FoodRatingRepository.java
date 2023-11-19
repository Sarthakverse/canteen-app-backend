package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.FoodRating;
import com.example.jwtauthorisedlogin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRatingRepository extends JpaRepository<FoodRating,Long> {
    Optional<FoodRating> findByUserAndFoodItem(User user, Food foodItem);
}