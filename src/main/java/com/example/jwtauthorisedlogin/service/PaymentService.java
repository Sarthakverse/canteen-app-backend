package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.PaymentEntity;
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

@Service
public class PaymentService {

    private final String razorpayKeyId;
    private final String razorpayKeySecret;
    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;

    public PaymentService(
            @Value("${rzp_key_id}") String razorpayKeyId,
            @Value("${rzp_key_secret}") String razorpayKeySecret, PaymentRepository paymentRepository) {
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
        this.paymentRepository = paymentRepository;
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
        try {
            JSONObject paymentRequest = new JSONObject();
            paymentRequest.put("amount", amount * 100);
            paymentRequest.put("currency", "INR");

            Payment payment = razorpayClient.payments.capture(paymentId, paymentRequest);

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
}
