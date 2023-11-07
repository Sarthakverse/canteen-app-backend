package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByCategory(Category category);
    Optional<Food> findById(Long id);
    List<Food> findByName(String name);
}
