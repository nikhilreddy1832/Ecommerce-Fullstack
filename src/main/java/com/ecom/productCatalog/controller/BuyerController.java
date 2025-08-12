package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.Product;
import com.ecom.productCatalog.model.User; // Import the User model
import com.ecom.productCatalog.service.ProductService;
import com.ecom.productCatalog.service.UserService; // Import the UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class BuyerController {

    @Autowired
    private ProductService productService;


    @Autowired
    private UserService userService;

    @GetMapping("/buyer/home")
    public String buyerHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);


        String email = userDetails.getUsername();

        // 2. Use the email to find the full User object.
        Optional<User> userOpt = userService.findByEmail(email);

        // 3. Add the actual username to the model.
        if (userOpt.isPresent()) {
            model.addAttribute("username", userOpt.get().getUsername());
        } else {
            // Fallback in case the user is not found (should not happen if logged in)
            model.addAttribute("username", email);
        }

        return "buyer-home";
    }
}