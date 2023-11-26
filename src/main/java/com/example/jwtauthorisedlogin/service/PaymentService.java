package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Cart;
import com.example.jwtauthorisedlogin.Entity.Food;
import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import com.example.jwtauthorisedlogin.Entity.PaymentEntity;
import com.example.jwtauthorisedlogin.payload.response.OrderHistoryResponse;
import com.example.jwtauthorisedlogin.repository.CartRepository;
import com.example.jwtauthorisedlogin.repository.FoodRepository;
import com.example.jwtauthorisedlogin.repository.OrderHistoryRepository;
import com.example.jwtauthorisedlogin.repository.PaymentRepository;
import com.example.jwtauthorisedlogin.user.User;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final String razorpayKeyId;
    private final String razorpayKeySecret;
    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final FoodRepository foodRepository;

    public PaymentService(
            @Value("${rzp_key_id}") String razorpayKeyId,
            @Value("${rzp_key_secret}") String razorpayKeySecret, PaymentRepository paymentRepository, CartRepository cartRepository, OrderHistoryRepository orderHistoryRepository, FoodRepository foodRepository) {
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.foodRepository = foodRepository;
        try {
            this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Error initializing RazorpayClient", e);
        }
    }

    public Order createOrder(double amount, String currency, String receipt) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",amount*100);
            orderRequest.put("currency",currency);
            orderRequest.put("receipt", receipt);
//            JSONObject notes = new JSONObject();
//            notes.put("notes_key_1","Tea, Earl Grey, Hot");
//            notes.put("notes_key_1","Tea, Earl Grey, Hot");
//            orderRequest.put("notes",notes);
            Order order=razorpayClient.orders.create(orderRequest);

            //get user email from jwt token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            PaymentEntity newPayment=new PaymentEntity();
            newPayment.setEmail(currentUser.getEmail());
            newPayment.setPrice(amount);
            newPayment.setDateTime(LocalDateTime.now());
            newPayment.setJsonOrder(order.toString());
            paymentRepository.save(newPayment);

            return order;
        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }

    public Payment capturePayment(String paymentId, double amount) {
        //saveOrderHistoryForCurrentUser();
        try {
            JSONObject paymentRequest = new JSONObject();
            paymentRequest.put("amount", amount * 100);
            paymentRequest.put("currency", "INR");

            Payment payment = razorpayClient.payments.capture(paymentId, paymentRequest);

            saveOrderHistoryForCurrentUser();

            return payment;
        } catch (RazorpayException e) {
            throw new RuntimeException("Error capturing Razorpay payment", e);
        }
    }
    public List<Order> getAllOrders() throws RazorpayException {
        JSONObject query = new JSONObject();
        query.put("count", 10);

        return razorpayClient.orders.fetchAll(query);
    }

    //order history
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
            orderHistory.setCurrentOrder(true);
            orderHistory.setCanteenId(cartItem.getFoodId().getCanteenId());
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

        List<OrderHistory> orderHistoryList = orderHistoryRepository.findAllByUserEmail(userEmail);

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

    public List<OrderHistoryResponse> getOrderHistoryByCanteenId(Long canteenId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();

        List<OrderHistory> orderHistoryList = orderHistoryRepository.findAllByCanteenId(canteenId);

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
    public List<OrderHistoryResponse> getCurrentOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();

        List<OrderHistory> orderHistoryList=orderHistoryRepository.findAllByCurrentOrder(true);

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

    public List<OrderHistoryResponse> getPreviousOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();

        List<OrderHistory> orderHistoryList=orderHistoryRepository.findAllByCurrentOrder(false);

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



