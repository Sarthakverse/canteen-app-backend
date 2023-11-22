package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import com.example.jwtauthorisedlogin.payload.response.OrderHistoryResponse;
import com.example.jwtauthorisedlogin.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-history")
@AllArgsConstructor
public class OrderHistoryController {

    private final OrderService orderService;

    @PostMapping("/save")
    public void saveOrderHistoryForCurrentUser() {
        orderService.saveOrderHistoryForCurrentUser();
    }

    @GetMapping("/history")
    public List<OrderHistoryResponse> getOrderHistoryForCurrentUser() {
        return orderService.getOrderHistoryForCurrentUser();
    }
}