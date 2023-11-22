package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByCategory(Category category);
    Optional<Food> findById(Long id);
    List<Food> findByNameContaining(String name);

    Food findByNameAndCanteenId(String name, Long canteenId);
}