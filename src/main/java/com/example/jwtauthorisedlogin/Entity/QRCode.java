package com.example.jwtauthorisedlogin.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "QRcode")
@Entity
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String qrCodeString;
    private String email;
}
