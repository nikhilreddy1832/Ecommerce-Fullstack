package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Displays the registration form.
     * It adds an empty User object to the model for the form to bind to.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Ensure there's a user object for the form
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        // Add roles to the model for the dropdown
        model.addAttribute("roles", User.Role.values());
        return "register"; // Returns register.html
    }

    /**
     * Processes the registration form submission.
     */
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        // Basic validation: Check if username or email already exists
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username already exists. Please choose another.");
            redirectAttributes.addFlashAttribute("user", user); // Send user data back to pre-fill the form
            return "redirect:/register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email is already registered. Please use another email.");
            redirectAttributes.addFlashAttribute("user", user); // Send user data back to pre-fill the form
            return "redirect:/register";
        }

        // If validation passes, register the user
        userService.registerUser(user);

        // Add a success message and redirect to the login page
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}