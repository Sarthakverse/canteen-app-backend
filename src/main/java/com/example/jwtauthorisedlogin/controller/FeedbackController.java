package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.FeedbackRequest;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
@AllArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/add-feedback")
    public ResponseEntity<MessageResponse> addFeedback(@RequestBody FeedbackRequest request) {
        var feedback = feedbackService.addFeedback(request);
        if(feedback.getMessage().contains("Feedback has been added"))
        {
            return ResponseEntity.ok(feedback);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete-feedback")
    public ResponseEntity<MessageResponse> deleteFeedback() {
        var feedback = feedbackService.deleteFeedback();
        if(feedback.getMessage().contains("Feedback has been deleted"))
        {
            return ResponseEntity.ok(feedback);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
