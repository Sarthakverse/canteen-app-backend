package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.CreatePaymentRequest;
import com.example.jwtauthorisedlogin.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Order> createOrder(@RequestBody CreatePaymentRequest request) {
        //Order order = new Order(new JSONObject());
        var order=paymentService.createOrder(request.getAmount(), request.getCurrency(), request.getReceipt());
        return ResponseEntity.ok(order);
//        try {
//            Order order = paymentService.createOrder(request.getAmount(), request.getCurrency(), request.getReceipt());
//            return ResponseEntity.ok(order);
//        } catch (RazorpayException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
    }

//    @PostMapping("/capture-payment")
//    public ResponseEntity<?> capturePayment(@RequestBody ) {
//        try {
//            Payment payment = paymentService.capturePayment(paymentId, amount);
//            return ResponseEntity.ok(payment);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error capturing payment");
//        }
//    }
}
