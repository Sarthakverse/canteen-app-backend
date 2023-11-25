package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.UserWishlist;
import com.example.jwtauthorisedlogin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {
    List<UserWishlist> findByUserEmail(String userEmail);
    boolean existsByUserAndFood(User user, Food food);

    Optional<UserWishlist> findByUserAndFood(User user, Food food);
    void deleteByFood(Food food);


}