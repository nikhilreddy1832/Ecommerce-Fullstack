package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.service.CartService;
import com.ecom.productCatalog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;


    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.addProductToCart(user, productId);
        return "redirect:/cart/view";
    }



    @GetMapping("/view")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("total", cartService.getCartTotal(user));
        return "cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/increase/{itemId}")
    public String increaseItem(@PathVariable Long itemId) {
        cartService.increaseQuantity(itemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/decrease/{itemId}")
    public String decreaseItem(@PathVariable Long itemId) {
        cartService.decreaseQuantity(itemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        cartService.clearCart(user);
        return "redirect:/cart/view";
    }
}
