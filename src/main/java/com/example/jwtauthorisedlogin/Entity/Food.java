package com.example.jwtauthorisedlogin.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "Food")
@Entity
public class Food{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Double price;
    //private Long canteenId;
    private String foodImage;
    private String description;
    @ManyToMany(mappedBy = "foods")
    private Set<Canteen> canteens = new HashSet<>();
}
