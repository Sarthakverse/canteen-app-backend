package com.example.jwtauthorisedlogin.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "canteen_food")
@Entity
public class CanteenFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
}
