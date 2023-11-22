package com.example.jwtauthorisedlogin.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "payment")
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Double price;
    private LocalDateTime dateTime;
    private String jsonOrder;
    private String jsonPayment;
}
