package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.Feedback;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.service.AgentService;
import com.dilshan.realestate.service.FeedbackService;
import com.dilshan.realestate.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final AgentService agentService;

    public FeedbackController(FeedbackService feedbackService, AgentService agentService) {
        this.feedbackService = feedbackService;
        this.agentService = agentService;
    }

    @PostMapping("/submit")
    public String submitFeedback(@RequestParam("agentId") Long agentId,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("comments") String comments,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.CLIENT) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in as a client to leave a review.");
            return "redirect:/login";
        }

        Optional<Agent> agentOpt = agentService.findById(agentId);
        if (agentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Agent not found.");
            return "redirect:/agents";
        }

        Feedback feedback = new Feedback((Client) currentUser, agentOpt.get(), rating, comments);
        feedbackService.submitFeedback(feedback);

        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback has been submitted.");
        return "redirect:/agents/" + agentId;
    }

    @PostMapping("/{id}/delete")
    public String deleteFeedback(@PathVariable("id") Long id,
                                 @RequestParam(value = "redirectUrl", required = false, defaultValue = "/agents") String redirectUrl,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Feedback> fOpt = feedbackService.findById(id);
        if (fOpt.isPresent()) {
            Feedback f = fOpt.get();
            if (currentUser.getRole() == Role.ADMIN || f.getClient().getId().equals(currentUser.getId())) {
                feedbackService.deleteFeedback(id);
                redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully.");
            }
        }
        return "redirect:" + redirectUrl;
    }
}
