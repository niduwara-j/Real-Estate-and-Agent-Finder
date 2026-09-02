package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.AppointmentStatus;
import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.service.AgentService;
import com.dilshan.realestate.service.AppointmentService;
import com.dilshan.realestate.service.PropertyService;
import com.dilshan.realestate.service.UserService;
import com.dilshan.realestate.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AgentService agentService;
    private final AppointmentService appointmentService;
    private final PropertyService propertyService;
    private final UserService userService;

    public AdminController(AgentService agentService,
                           AppointmentService appointmentService,
                           PropertyService propertyService,
                           UserService userService) {
        this.agentService = agentService;
        this.appointmentService = appointmentService;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    private boolean checkAdmin(HttpSession session) {
        return SessionHelper.hasRole(session, Role.ADMIN);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/login";

        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        // Stats matching SLIIT UI
        model.addAttribute("totalAgents", agentService.countTotalAgents());
        model.addAttribute("verifiedAgents", agentService.countVerifiedAgents());
        model.addAttribute("availableAgents", agentService.countAvailableAgents());
        model.addAttribute("totalAppointments", appointmentService.countTotalAppointments());
        model.addAttribute("totalProperties", propertyService.countTotalProperties());

        // Lists
        model.addAttribute("agentsAwaitingVerification", agentService.getUnverifiedAgents());
        model.addAttribute("verifiedAgentsList", agentService.getVerifiedAgents());
        model.addAttribute("upcomingAppointments", appointmentService.getAllAppointments());

        return "admin/dashboard";
    }

    @GetMapping("/agents")
    public String manageAgents(@RequestParam(value = "keyword", required = false) String keyword,
                               Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/login";

        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        List<Agent> agents = (keyword != null && !keyword.isBlank())
                ? agentService.searchAndFilter(keyword, null, null, "rating")
                : agentService.getAllAgents();

        model.addAttribute("agents", agents);
        model.addAttribute("keyword", keyword);
        return "admin/agents";
    }

    @PostMapping("/agents/{id}/verify")
    public String verifyAgent(@PathVariable("id") Long id,
                              @RequestParam("verified") boolean verified,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) return "redirect:/login";

        agentService.verifyAgent(id, verified);
        redirectAttributes.addFlashAttribute("successMessage", "Agent verification status updated.");
        return "redirect:/admin/agents";
    }

    @GetMapping("/appointments")
    public String manageAppointments(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/login";

        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("statuses", AppointmentStatus.values());

        return "admin/appointments";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session)) return "redirect:/login";

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User account deleted successfully.");
        return "redirect:/admin/dashboard";
    }
}
