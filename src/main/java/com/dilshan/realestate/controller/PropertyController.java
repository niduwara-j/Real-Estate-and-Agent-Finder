package com.dilshan.realestate.controller;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Property;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.service.AgentService;
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
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final AgentService agentService;

    public PropertyController(PropertyService propertyService, AgentService agentService) {
        this.propertyService = propertyService;
        this.agentService = agentService;
    }

    @GetMapping
    public String listProperties(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "type", required = false) PropertyType type,
                                 @RequestParam(value = "minPrice", required = false) Double minPrice,
                                 @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                 @RequestParam(value = "sortBy", required = false, defaultValue = "price_asc") String sortBy,
                                 Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        List<Property> properties = propertyService.searchAndFilter(keyword, type, minPrice, maxPrice, sortBy);

        model.addAttribute("properties", properties);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedType", type);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("propertyTypes", PropertyType.values());

        return "properties";
    }

    @GetMapping("/{id}")
    public String propertyDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        model.addAttribute("currentUser", currentUser);

        Optional<Property> propertyOpt = propertyService.findById(id);
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();
        model.addAttribute("property", property);
        model.addAttribute("agent", property.getAgent());

        return "property-detail";
    }

    @GetMapping("/add")
    public String showAddPropertyForm(Model model, HttpSession session) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.AGENT) {
            return "redirect:/login";
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("propertyTypes", PropertyType.values());
        return "add-property";
    }

    @PostMapping("/add")
    public String handleAddProperty(@RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("price") double price,
                                    @RequestParam("address") String address,
                                    @RequestParam("city") String city,
                                    @RequestParam("state") String state,
                                    @RequestParam("zipCode") String zipCode,
                                    @RequestParam("propertyType") PropertyType propertyType,
                                    @RequestParam("bedrooms") int bedrooms,
                                    @RequestParam("bathrooms") int bathrooms,
                                    @RequestParam("areaSqFt") double areaSqFt,
                                    @RequestParam(value = "imageUrl", required = false) String imageUrl,
                                    @RequestParam("status") String status,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null || currentUser.getRole() != Role.AGENT) {
            return "redirect:/login";
        }

        Optional<Agent> agentOpt = agentService.findById(currentUser.getId());
        if (agentOpt.isEmpty()) {
            return "redirect:/login";
        }

        Property property = new Property(
                title, description, price, address, city, state, zipCode,
                propertyType, bedrooms, bathrooms, areaSqFt, imageUrl, status, agentOpt.get()
        );
        propertyService.save(property);
        redirectAttributes.addFlashAttribute("successMessage", "Property listing published successfully!");
        return "redirect:/agent/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String deleteProperty(@PathVariable("id") Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = SessionHelper.getLoggedInUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(id);
        if (propertyOpt.isPresent()) {
            Property p = propertyOpt.get();
            if (currentUser.getRole() == Role.ADMIN || p.getAgent().getId().equals(currentUser.getId())) {
                propertyService.delete(id);
                redirectAttributes.addFlashAttribute("successMessage", "Property deleted successfully.");
            }
        }
        return currentUser.getRole() == Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/agent/dashboard";
    }
}
