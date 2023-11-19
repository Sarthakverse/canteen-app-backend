package com.example.jwtauthorisedlogin.Entity;

import com.example.jwtauthorisedlogin.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "food_rating")
public class FoodRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private Food foodItem;

    private int rating;

}
