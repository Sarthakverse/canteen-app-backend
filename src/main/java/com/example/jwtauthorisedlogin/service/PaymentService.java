package com.example.jwtauthorisedlogin.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final String razorpayKeyId;
    private final String razorpayKeySecret;
    private final RazorpayClient razorpayClient;

    public PaymentService(
            @Value("${rzp_key_id}") String razorpayKeyId,
            @Value("${rzp_key_secret}") String razorpayKeySecret) {
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
        try {
            this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Error initializing RazorpayClient", e);
        }
    }

    public Order createOrder(double amount, String currency, String receipt) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",amount);
            orderRequest.put("currency",currency);
            orderRequest.put("receipt", receipt);
//            JSONObject notes = new JSONObject();
//            notes.put("notes_key_1","Tea, Earl Grey, Hot");
//            notes.put("notes_key_1","Tea, Earl Grey, Hot");
//            orderRequest.put("notes",notes);
            Order order=razorpayClient.orders.create(orderRequest);
            return order;
        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }

    public Payment capturePayment(String paymentId, double amount) {
        try {
            JSONObject options = new JSONObject();
            options.put("amount", amount * 100);

            return razorpayClient.payments.capture(paymentId, options);
        } catch (RazorpayException e) {
            throw new RuntimeException("Error capturing Razorpay payment", e);
        }
    }
}
