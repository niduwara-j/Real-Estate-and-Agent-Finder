package com.dilshan.realestate.service;

import com.dilshan.realestate.dsa.AgentBST;
import com.dilshan.realestate.dsa.SortEngine;
import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.enums.Specialization;
import com.dilshan.realestate.repository.AgentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public List<Agent> getVerifiedAgents() {
        return agentRepository.findByIsVerifiedTrue();
    }

    public List<Agent> getUnverifiedAgents() {
        return agentRepository.findByIsVerifiedFalse();
    }

    public Optional<Agent> findById(Long id) {
        return agentRepository.findById(id);
    }

    public Agent save(Agent agent) {
        return agentRepository.save(agent);
    }

    public void verifyAgent(Long id, boolean isVerified) {
        agentRepository.findById(id).ifPresent(agent -> {
            agent.setVerified(isVerified);
            agentRepository.save(agent);
        });
    }

    public void toggleAvailability(Long id) {
        agentRepository.findById(id).ifPresent(agent -> {
            agent.setAvailable(!agent.isAvailable());
            agentRepository.save(agent);
        });
    }

    /**
     * Search and Filter agents with DSA Tree and Sorting integration.
     */
    public List<Agent> searchAndFilter(String keyword, Specialization spec, Double minRating, String sortBy) {
        List<Agent> agents = agentRepository.searchAgents(
                (keyword != null && !keyword.isBlank()) ? keyword.trim() : null,
                spec,
                minRating
        );

        // Apply DSA Sorting based on request
        if ("rating".equalsIgnoreCase(sortBy)) {
            return SortEngine.selectionSortByRating(agents, true);
        } else if ("experience".equalsIgnoreCase(sortBy)) {
            return SortEngine.quickSortByExperience(agents, true);
        }

        return agents;
    }

    /**
     * Build an in-memory Binary Search Tree (BST) of all verified agents.
     */
    public AgentBST buildAgentBST() {
        AgentBST bst = new AgentBST();
        List<Agent> agents = agentRepository.findAll();
        for (Agent a : agents) {
            bst.insert(a);
        }
        return bst;
    }

    public long countTotalAgents() {
        return agentRepository.count();
    }

    public long countVerifiedAgents() {
        return agentRepository.findByIsVerifiedTrue().size();
    }

    public long countAvailableAgents() {
        return agentRepository.findByIsAvailableTrue().size();
    }
}
