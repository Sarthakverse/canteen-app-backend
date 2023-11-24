package com.example.jwtauthorisedlogin.payload.response;

import com.example.jwtauthorisedlogin.Entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String feedback;
    private Integer rating;
    private String userEmail;

    public FeedbackResponse(Feedback feedback) {
        this.id = feedback.getId();
        this.feedback = feedback.getFeedback();
        this.rating = feedback.getRating();
        this.userEmail = feedback.getUser().getEmail();
    }
}
