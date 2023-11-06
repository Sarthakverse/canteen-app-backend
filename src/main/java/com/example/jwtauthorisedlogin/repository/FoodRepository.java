package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByCategory(Category category);
}
