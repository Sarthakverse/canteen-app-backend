package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    QRCode findByEmail(String email);
}
