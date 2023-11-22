package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
    Optional<PaymentEntity> findAllByEmail(String email);
    Optional<PaymentEntity> findAllByEmailAndJsonPayment(String email, String jsonPayment);
}
