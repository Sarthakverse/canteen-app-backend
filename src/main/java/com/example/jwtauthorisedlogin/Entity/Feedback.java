package com.example.jwtauthorisedlogin.Entity;

import com.example.jwtauthorisedlogin.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private String feedback;

    private Integer rating;

}
