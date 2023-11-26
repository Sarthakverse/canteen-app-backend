package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findAllByUserEmail(String userEmail);
    List<OrderHistory> findAllByCanteenId(Long canteenId);

    List<OrderHistory> findAllByCurrentOrder(Boolean currentOrder);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM order_history WHERE food_item_id = :foodItemId", nativeQuery = true)
    void deleteOrderHistoryByFoodId(@Param("foodItemId") Long foodItemId);
}
