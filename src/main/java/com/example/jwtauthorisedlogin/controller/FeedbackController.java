package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.Entity.Feedback;
import com.example.jwtauthorisedlogin.payload.request.FeedbackRequest;
import com.example.jwtauthorisedlogin.payload.response.FeedbackResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@AllArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/add-feedback")
    public ResponseEntity<MessageResponse> addFeedback(@Valid @RequestBody FeedbackRequest request) {
        var feedback = feedbackService.addFeedback(request);
        if (feedback.getMessage().contains("Feedback has been added")) {
            return ResponseEntity.ok(feedback);
        }else if(feedback.getMessage().contains("Feedback has been updated")){
            return ResponseEntity.ok(feedback);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete-feedback")
    public ResponseEntity<MessageResponse> deleteFeedback() {
        var feedback = feedbackService.deleteFeedback();
        if (feedback.getMessage().contains("Feedback has been deleted")) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-feedback")
    public ResponseEntity<List<FeedbackResponse>> getFeedback() {
        List<FeedbackResponse> feedback = feedbackService.getFeedback();
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
