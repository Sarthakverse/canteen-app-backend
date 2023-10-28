package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Email.OtpEntity;
import com.example.jwtauthorisedlogin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity,Long> {
    Optional<OtpEntity> findByEmail(String email);
}
