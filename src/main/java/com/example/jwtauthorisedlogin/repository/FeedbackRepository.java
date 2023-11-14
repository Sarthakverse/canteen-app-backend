package com.example.jwtauthorisedlogin.repository;

import com.example.jwtauthorisedlogin.Entity.Feedback;
import com.example.jwtauthorisedlogin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

    Optional<Feedback> findByUser(User user);
}
