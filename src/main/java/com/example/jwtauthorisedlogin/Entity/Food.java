package com.example.jwtauthorisedlogin.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
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
    private String foodImageName;
    @Transient
    private MultipartFile foodImage;
    private String description;
    @ElementCollection
    private List<String> ingredients;
    @ElementCollection
    private List<String> IngredientImageList;
    @JsonIgnore
    @ManyToMany(mappedBy = "foods",fetch = FetchType.LAZY)
    private Set<Canteen> canteens = new HashSet<>();
}
