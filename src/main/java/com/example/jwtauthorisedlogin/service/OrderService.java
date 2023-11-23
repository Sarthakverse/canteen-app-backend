package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import com.example.jwtauthorisedlogin.payload.response.OrderHistoryResponse;
import com.example.jwtauthorisedlogin.repository.CartRepository;
import com.example.jwtauthorisedlogin.repository.OrderHistoryRepository;
import com.example.jwtauthorisedlogin.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderHistoryRepository orderHistoryRepository;
    private final CartRepository cartRepository;

    public void saveOrderHistoryForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        String userEmail = currentUser.getEmail();

        List<Cart> userCartItems = cartRepository.findByUserEmail(userEmail);

        for (Cart cartItem : userCartItems) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setFoodItemName(cartItem.getFoodItemName());
            orderHistory.setQuantity(cartItem.getQuantity());
            orderHistory.setPrice(cartItem.getPrice());
            orderHistory.setUser(cartItem.getUser());
            orderHistory.setFoodId(cartItem.getFoodId());
            orderHistory.setOrderDateTime(LocalDateTime.now());

            orderHistoryRepository.save(orderHistory);
        }
        cartRepository.deleteByUserEmail(userEmail);
    }

    public List<OrderHistoryResponse> getOrderHistoryForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();

        List<OrderHistory> orderHistoryList = orderHistoryRepository.findByUserEmail(userEmail);

        return orderHistoryList.stream()
                .map(orderHistory -> new OrderHistoryResponse(
                        orderHistory.getId(),
                        orderHistory.getFoodItemName(),
                        orderHistory.getFoodId().getId(),
                        orderHistory.getQuantity(),
                        orderHistory.getPrice(),
                        orderHistory.getUser().getEmail(),
                        orderHistory.getOrderDateTime()
                ))
                .collect(Collectors.toList());
    }
}