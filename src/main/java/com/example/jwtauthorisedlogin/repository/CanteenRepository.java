package com.example.jwtauthorisedlogin.repository;


import com.example.jwtauthorisedlogin.Entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CanteenRepository extends JpaRepository<Canteen,Long> {
    Optional<Canteen> findById(Long id);
    Canteen findByName(String name);


}
