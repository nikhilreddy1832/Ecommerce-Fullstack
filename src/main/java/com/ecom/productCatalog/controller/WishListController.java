package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.model.WishListProduct;
import com.ecom.productCatalog.service.UserService;
import com.ecom.productCatalog.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    // Utility method to fetch the current user
    private Optional<User> getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) return Optional.empty();
        // ✅ CORRECTED: Search by email, since the UserDetails username is the email.
        return userService.findByEmail(userDetails.getUsername());
    }

    // Display the wishlist page
    @GetMapping
    public String viewWishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<User> userOpt = getCurrentUser(userDetails);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        List<WishListProduct> wishlistItems = wishListService.getWishlistProducts(userOpt.get());
        model.addAttribute("wishlistItems", wishlistItems);
        return "wishlist";
    }

    // Add product to wishlist
    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOpt = getCurrentUser(userDetails);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        wishListService.addProductToWishlist(userOpt.get(), productId);
        return "redirect:/wishlist";
    }

    // Remove product from wishlist
    @PostMapping("/remove/{wishlistProductId}")
    public String removeFromWishlist(@PathVariable Long wishlistProductId) {
        wishListService.removeProductFromWishlist(wishlistProductId);
        return "redirect:/wishlist";
    }
}