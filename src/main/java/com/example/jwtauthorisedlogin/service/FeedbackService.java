package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.Feedback;
import com.example.jwtauthorisedlogin.payload.request.FeedbackRequest;
import com.example.jwtauthorisedlogin.payload.response.FeedbackResponse;
import com.example.jwtauthorisedlogin.payload.response.MessageResponse;
import com.example.jwtauthorisedlogin.repository.FeedbackRepository;
import com.example.jwtauthorisedlogin.repository.UserRepository;
import com.example.jwtauthorisedlogin.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    public MessageResponse addFeedback(FeedbackRequest feedbackRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);

        if (user != null) {
            Feedback existingFeedback = feedbackRepository.findByUser(user).orElse(null);
            if (existingFeedback != null) {
                existingFeedback.setFeedback(feedbackRequest.getFeedback());
                existingFeedback.setRating(feedbackRequest.getRating());
                feedbackRepository.save(existingFeedback);
                return MessageResponse.builder().message("Feedback has been updated").build();
            } else {
                var feedback = new Feedback();
                feedback.setFeedback(feedbackRequest.getFeedback());
                feedback.setUser(user);
                feedback.setRating(feedbackRequest.getRating());
                feedbackRepository.save(feedback);
                return MessageResponse.builder().message("Feedback has been added").build();
            }
        } else {
            return MessageResponse.builder().message("Feedback has not been added").build();
        }
    }

    public MessageResponse deleteFeedback() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        var user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);

        if (user != null) {
            var feedbackExists = feedbackRepository.findByUser(user).orElse(null);

            if (feedbackExists != null) {
                feedbackRepository.delete(feedbackExists);
                return MessageResponse.builder().message("Feedback has been deleted").build();
            } else {
                return MessageResponse.builder().message("No feedback found for the user").build();
            }
        } else {
            return MessageResponse.builder().message("User not found").build();
        }
    }

    public List<FeedbackResponse> getFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        List<Feedback> filteredFeedbackList = feedbackList.stream()
                .filter(feedback -> feedback.getFeedback() != null)
                .collect(Collectors.toList());

        return filteredFeedbackList.stream()
                .map(FeedbackResponse::new)
                .collect(Collectors.toList());

    }

}

