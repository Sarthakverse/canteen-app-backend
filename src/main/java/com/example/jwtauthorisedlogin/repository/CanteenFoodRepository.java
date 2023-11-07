package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.CanteenFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CanteenFoodRepository extends JpaRepository<CanteenFood,Long> {
    @Query("SELECT cf.food.id FROM CanteenFood cf WHERE cf.canteen.id = :canteenId")
    Set<Long> findFoodIdsByCanteenId(@Param("canteenId") Long canteenId);
}
