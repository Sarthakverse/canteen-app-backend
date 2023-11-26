package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.payload.response.GetCartItemResponse;
import com.example.jwtauthorisedlogin.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUserEmail(String email);
    Optional<Cart> findById(Long id);

    @Query("SELECT new com.example.jwtauthorisedlogin.payload.response.GetCartItemResponse(c.id, c.foodItemName, c.quantity, c.price , c.foodId.canteenId,c.foodId.averageRating) FROM Cart c WHERE c.user.email = :email")
    List<GetCartItemResponse> findCartResponseByUserEmail(@Param("email") String email);

    Optional<Cart> findByFoodIdAndUser(Food foodId,User user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Cart c WHERE c.user.email = :userEmail")
    void deleteByUserEmail(@Param("userEmail") String userEmail);
    Optional<Cart> findByFoodIdIdAndUserEmail(Long foodId, String userEmail);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM _cart_ WHERE id=?1",nativeQuery = true)
    void deleteByFoodId(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM _cart_ WHERE food_item_id = :foodItemId", nativeQuery = true)
    void deleteCartByFoodId(@Param("foodItemId") Long foodItemId);


}

