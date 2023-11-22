package com.example.jwtauthorisedlogin.payload.response;

import com.example.jwtauthorisedlogin.Entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponse {
    private Long id;
    private String foodItemName;
    private Long foodId;
    private Integer quantity;
    private Double price;
    private String userEmail;
    private LocalDateTime orderDateTime;
}
