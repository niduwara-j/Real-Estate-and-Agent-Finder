package com.dilshan.realestate.repository;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByAgentOrderByCreatedAtDesc(Agent agent);
}
