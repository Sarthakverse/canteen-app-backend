package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import com.example.jwtauthorisedlogin.payload.request.CapturePaymentRequest;
import com.example.jwtauthorisedlogin.payload.request.CreatePaymentRequest;
import com.example.jwtauthorisedlogin.payload.response.OrderHistoryResponse;
import com.example.jwtauthorisedlogin.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> check(@RequestBody CreatePaymentRequest request){
        return ResponseEntity.ok(request.getAmount()+" "+request.getCurrency()+" "+request.getReceipt());
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreatePaymentRequest request) {

        try {
            Order order = paymentService.createOrder(request.getAmount(), request.getCurrency(), request.getReceipt());
            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/capture-payment")
    public ResponseEntity<?> capturePayment(@RequestBody CapturePaymentRequest request) {
        try {
            Payment payment = paymentService.capturePayment(request.getPaymentId(), request.getAmount());
            return ResponseEntity.ok(payment.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error capturing payment");
        }
    }
    @GetMapping("/get-all-orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Order> orders = paymentService.getAllOrders();
            return ResponseEntity.ok(orders.toString());
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching orders");
        }
    }

    @GetMapping("/order-history")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistoryForCurrentUser() {
        try{
            return ResponseEntity.ok(paymentService.getOrderHistoryForCurrentUser());
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/current-order")
    public ResponseEntity<List<OrderHistoryResponse>> getCurrentOrder(){
        try{
            return ResponseEntity.ok(paymentService.getCurrentOrder());
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/previous-order")
    public ResponseEntity<List<OrderHistoryResponse>> getPreviousOrder(){
        try{
            return ResponseEntity.ok(paymentService.getPreviousOrder());
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
