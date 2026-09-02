package com.dilshan.realestate.service;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Feedback;
import com.dilshan.realestate.repository.AgentRepository;
import com.dilshan.realestate.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AgentRepository agentRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, AgentRepository agentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.agentRepository = agentRepository;
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbacksForAgent(Agent agent) {
        return feedbackRepository.findByAgentOrderByCreatedAtDesc(agent);
    }

    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback submitFeedback(Feedback feedback) {
        Feedback saved = feedbackRepository.save(feedback);
        // Refresh and recalculate agent rating
        Agent agent = saved.getAgent();
        if (agent != null) {
            List<Feedback> allForAgent = feedbackRepository.findByAgentOrderByCreatedAtDesc(agent);
            agent.setFeedbacks(allForAgent);
            agent.recalculateRating();
            agentRepository.save(agent);
        }
        return saved;
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.findById(id).ifPresent(f -> {
            Agent agent = f.getAgent();
            feedbackRepository.delete(f);
            if (agent != null) {
                List<Feedback> allForAgent = feedbackRepository.findByAgentOrderByCreatedAtDesc(agent);
                agent.setFeedbacks(allForAgent);
                agent.recalculateRating();
                agentRepository.save(agent);
            }
        });
    }

    public long countTotalFeedbacks() {
        return feedbackRepository.count();
    }
}
