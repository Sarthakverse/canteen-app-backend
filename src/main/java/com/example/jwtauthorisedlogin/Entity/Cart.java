package com.example.jwtauthorisedlogin.Entity;

import com.example.jwtauthorisedlogin.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_Cart_")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String foodItemName;
    private Integer quantity;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_item_id",referencedColumnName = "id")
    private Food foodId;
}

