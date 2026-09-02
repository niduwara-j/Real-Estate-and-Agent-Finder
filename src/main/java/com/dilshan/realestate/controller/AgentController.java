package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.Specialization;
import com.dilshan.realestate.service.AgentService;
import com.dilshan.realestate.service.FeedbackService;
import com.dilshan.realestate.service.PropertyService;
import com.dilshan.realestate.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;
    private final PropertyService propertyService;
    private final FeedbackService feedbackService;

    public AgentController(AgentService agentService,
                           PropertyService propertyService,
                           FeedbackService feedbackService) {
        this.agentService = agentService;
        this.propertyService = propertyService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String listAgents(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "spec", required = false) Specialization spec,
                             @RequestParam(value = "minRating", required = false) Double minRating,
                             @RequestParam(value = "sortBy", required = false, defaultValue = "rating") String sortBy,
                             Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        List<Agent> agents = agentService.searchAndFilter(keyword, spec, minRating, sortBy);

        model.addAttribute("agents", agents);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedSpec", spec);
        model.addAttribute("selectedRating", minRating);
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("specializations", Specialization.values());

        return "agents";
    }

    @GetMapping("/{id}")
    public String agentProfile(@PathVariable("id") Long id, Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        Optional<Agent> agentOpt = agentService.findById(id);
        if (agentOpt.isEmpty()) {
            return "redirect:/agents";
        }

        Agent agent = agentOpt.get();
        model.addAttribute("agent", agent);
        model.addAttribute("properties", propertyService.findByAgent(agent));
        model.addAttribute("feedbacks", feedbackService.getFeedbacksForAgent(agent));

        return "agent-profile";
    }

    @PostMapping("/{id}/toggle-availability")
    public String toggleAvailability(@PathVariable("id") Long id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || !currentUser.getId().equals(id)) {
            return "redirect:/login";
        }

        agentService.toggleAvailability(id);
        redirectAttributes.addFlashAttribute("successMessage", "Availability status updated successfully.");
        return "redirect:/agent/dashboard";
    }
}
