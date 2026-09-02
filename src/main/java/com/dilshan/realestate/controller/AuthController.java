package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.model.enums.Specialization;
import com.dilshan.realestate.service.UserService;
import com.dilshan.realestate.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "registered", required = false) String registered,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model, HttpSession session) {
        if (SessionHelper.isLoggedIn(session)) {
            return redirectBasedOnRole(SessionHelper.getLoggedInUser(session));
        }

        if (error != null) model.addAttribute("errorMessage", "Invalid email or password.");
        if (registered != null) model.addAttribute("successMessage", "Registration successful! Please sign in.");
        if (logout != null) model.addAttribute("infoMessage", "You have been safely logged out.");

        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.authenticate(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            SessionHelper.setLoggedInUser(session, user);
            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + user.getName() + "!");
            return redirectBasedOnRole(user);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid credentials. Please try again.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        if (SessionHelper.isLoggedIn(session)) {
            return redirectBasedOnRole(SessionHelper.getLoggedInUser(session));
        }
        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("propertyTypes", PropertyType.values());
        return "register";
    }

    @PostMapping("/register/client")
    public String registerClient(@RequestParam("name") String name,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("contactNumber") String contactNumber,
                                 @RequestParam(value = "preferredPropertyType", required = false) PropertyType propertyType,
                                 @RequestParam(value = "preferredLocation", required = false) String location,
                                 RedirectAttributes redirectAttributes) {
        try {
            Client client = new Client(name, email, password, contactNumber, propertyType, location);
            userService.registerClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/register/agent")
    public String registerAgent(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("contactNumber") String contactNumber,
                                @RequestParam("licenseNumber") String licenseNumber,
                                @RequestParam("specialization") Specialization specialization,
                                @RequestParam("yearsOfExperience") int experience,
                                @RequestParam("serviceAreas") String serviceAreas,
                                @RequestParam(value = "bio", required = false) String bio,
                                RedirectAttributes redirectAttributes) {
        try {
            Agent agent = new Agent(name, email, password, contactNumber, licenseNumber, specialization, experience, serviceAreas, bio);
            userService.registerAgent(agent);
            redirectAttributes.addFlashAttribute("successMessage", "Agent registration submitted! An admin will review and verify your license shortly.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionHelper.logout(session);
        redirectAttributes.addFlashAttribute("infoMessage", "You have been successfully logged out.");
        return "redirect:/login";
    }

    private String redirectBasedOnRole(User user) {
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else if (user.getRole() == Role.AGENT) {
            return "redirect:/agent/dashboard";
        } else {
            return "redirect:/client/dashboard";
        }
    }
}
