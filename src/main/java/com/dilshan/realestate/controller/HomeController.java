package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.Specialization;
import com.dilshan.realestate.service.AgentService;
import com.dilshan.realestate.service.AppointmentService;
import com.dilshan.realestate.service.PropertyService;
import com.dilshan.realestate.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AgentService agentService;
    private final PropertyService propertyService;
    private final AppointmentService appointmentService;

    public HomeController(AgentService agentService,
                          PropertyService propertyService,
                          AppointmentService appointmentService) {
        this.agentService = agentService;
        this.propertyService = propertyService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        // Featured Verified Agents & Properties
        model.addAttribute("featuredAgents", agentService.getVerifiedAgents());
        model.addAttribute("recentProperties", propertyService.getAllProperties());
        model.addAttribute("specializations", Specialization.values());

        // Platform Statistics
        model.addAttribute("totalAgents", agentService.countTotalAgents());
        model.addAttribute("verifiedAgents", agentService.countVerifiedAgents());
        model.addAttribute("totalProperties", propertyService.countTotalProperties());
        model.addAttribute("totalAppointments", appointmentService.countTotalAppointments());

        return "home";
    }

    @GetMapping("/privacy")
    public String privacyPolicy(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getLoggedInUser(session));
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsOfService(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getLoggedInUser(session));
        return "terms";
    }

    @GetMapping("/security")
    public String securityOverview(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getLoggedInUser(session));
        return "security";
    }
}
