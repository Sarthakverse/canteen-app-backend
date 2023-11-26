package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Category;
import com.example.jwtauthorisedlogin.Entity.Food;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByCategory(Category category);
    Optional<Food> findById(Long id);

    Food findByNameAndCanteenId(String name, Long canteenId);
    List<Food> findAllByNameContainingIgnoreCase(String foodName);

    List<Food> findAllByCanteenIdAndIsAvailable(Long canteenId, Boolean isAvailable);


    //List<Food> findAllByCanteenIdAndNameContainingIgnoreCaseAndCategoryAndPriceBetweenAndVegAndAverageRatingAfter(Long id,String name, Category category, Double lowPrice, Double highPrice, Boolean veg, Double average_rating);


    @Query("SELECT f FROM Food f " +
            "WHERE (:canteenId IS NULL OR f.canteenId = :canteenId) " +
            "AND LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND (:category IS NULL OR f.category = :category ) " +
            "AND f.price BETWEEN :lowPrice AND :highPrice " +
            "AND ((:veg IS NULL AND f.veg IS NOT NULL) OR f.veg = :veg) " +
            "AND f.averageRating >= :averageRating")
    List<Food> findFoodsByCriteria(
            @Param("canteenId") Long canteenId,
            @Param("name") String name,
            @Param("category") Category category,
            @Param("lowPrice") Double lowPrice,
            @Param("highPrice") Double highPrice,
            @Param("veg") Boolean veg,
            @Param("averageRating") Double averageRating
    );


}