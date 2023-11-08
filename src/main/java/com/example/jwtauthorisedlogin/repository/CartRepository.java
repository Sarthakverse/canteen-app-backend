package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long > {
}
