package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Appointment;
import com.dilshan.realestate.model.Client;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AgentService agentService;
    private final UserService userService;
    private final PropertyService propertyService;

    public AppointmentController(AppointmentService appointmentService,
                                 AgentService agentService,
                                 UserService userService,
                                 PropertyService propertyService) {
        this.appointmentService = appointmentService;
        this.agentService = agentService;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam("agentId") Long agentId,
                                  @RequestParam("appointmentDate") String dateStr,
                                  @RequestParam("appointmentTime") String timeStr,
                                  @RequestParam(value = "notes", required = false) String notes,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.CLIENT) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in as a client to book an appointment.");
            return "redirect:/login";
        }

        Optional<Agent> agentOpt = agentService.findById(agentId);
        if (agentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selected agent not found.");
            return "redirect:/agents";
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalTime time = LocalTime.parse(timeStr);

            Appointment appointment = new Appointment((Client) currentUser, agentOpt.get(), date, time, notes);
            appointmentService.bookAppointment(appointment);

            redirectAttributes.addFlashAttribute("successMessage", "Appointment request submitted! Your agent will review and confirm.");
            return "redirect:/client/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid appointment date or time: " + e.getMessage());
            return "redirect:/agents/" + agentId;
        }
    }

    @GetMapping("/client/dashboard")
    public String clientDashboard(Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.CLIENT) {
            return "redirect:/login";
        }

        Client client = (Client) currentUser;
        model.addAttribute("currentUser", client);
        model.addAttribute("appointments", appointmentService.findByClient(client));
        model.addAttribute("recommendedAgents", agentService.getVerifiedAgents());

        return "client-dashboard";
    }

    @GetMapping("/agent/dashboard")
    public String agentDashboard(Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.AGENT) {
            return "redirect:/login";
        }

        Optional<Agent> agentOpt = agentService.findById(currentUser.getId());
        if (agentOpt.isEmpty()) return "redirect:/login";

        Agent agent = agentOpt.get();
        model.addAttribute("currentUser", agent);
        model.addAttribute("appointments", appointmentService.findByAgent(agent));
        model.addAttribute("pendingQueue", appointmentService.getPendingQueueForAgent(agent).toList());
        model.addAttribute("myProperties", propertyService.findByAgent(agent));

        return "agent-dashboard";
    }

    @PostMapping("/appointments/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") AppointmentStatus status,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        appointmentService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated to " + status + ".");

        if (currentUser.getRole() == Role.ADMIN) {
            return "redirect:/admin/appointments";
        } else if (currentUser.getRole() == Role.AGENT) {
            return "redirect:/agent/dashboard";
        } else {
            return "redirect:/client/dashboard";
        }
    }
}
