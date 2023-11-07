package com.example.jwtauthorisedlogin.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long canteenId;
    private String foodImage;
    private String description;
    @JsonIgnore
    @ManyToMany(mappedBy = "foods",fetch = FetchType.LAZY)
    private Set<Canteen> canteens = new HashSet<>();
}
