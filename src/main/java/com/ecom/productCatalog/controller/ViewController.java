package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public String login() {
        return "login"; // Returns login.html
    }

    // This method now correctly resides in a @Controller
    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByEmail(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("username", user.getUsername()));
        }
        return "home"; // This will now correctly resolve to your home.html template
    }
}