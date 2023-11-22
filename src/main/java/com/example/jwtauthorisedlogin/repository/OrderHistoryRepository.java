package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByUserEmail(String userEmail);
}
