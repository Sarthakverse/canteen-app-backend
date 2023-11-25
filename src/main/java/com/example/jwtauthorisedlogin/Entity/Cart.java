package com.example.jwtauthorisedlogin.Entity;

import com.example.jwtauthorisedlogin.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_Cart_")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String foodItemName;
    private Integer quantity;
    private Double price;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "food_item_id",referencedColumnName = "id")
    private Food foodId;
}

