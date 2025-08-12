package com.ecom.productCatalog.service;

import com.ecom.productCatalog.model.*;
import com.ecom.productCatalog.repository.CartItemRepository;
import com.ecom.productCatalog.repository.CartRepository;
import com.ecom.productCatalog.repository.ProductRepository;
import com.ecom.productCatalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            user.setCart(newCart);
            return cartRepository.save(newCart);
        });
    }


    @Transactional
    public void addProductToCart(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getCartByUser(user);

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, 1);
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void increaseQuantity(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
    }

    @Transactional
    public void decreaseQuantity(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }

    public List<CartItem> getCartItems(User user) {
        Cart cart = getCartByUser(user);
        return cartItemRepository.findByCart(cart);
    }

    public double getCartTotal(User user) {
        return getCartItems(user).stream()
                .mapToDouble(i -> i.getProduct().getPrice().doubleValue() * i.getQuantity())
                .sum();
    }

    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cartItemRepository.deleteAllByCart(cart);
    }
}
