package com.example.jwtauthorisedlogin.Entity;

import com.example.jwtauthorisedlogin.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Order_History")
@Data
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodItemName;
    private Integer quantity;
    private Double price;
    private Boolean currentOrder;
    private Long canteenId;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_item_id", referencedColumnName = "id")
    private Food foodId;

    private LocalDateTime orderDateTime;
}
