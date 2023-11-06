package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanteenRepository extends JpaRepository<Canteen,Long> {
}
