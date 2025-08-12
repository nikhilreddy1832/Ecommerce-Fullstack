package com.ecom.productCatalog.service;

import com.ecom.productCatalog.model.Cart;
import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.model.WishList;
import com.ecom.productCatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional // Ensures atomicity
    public User registerUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create and associate Cart and Wishlist
        // The User entity should have helper methods for this
        user.setCart(new Cart(user));
        user.setWishlist(new WishList(user));

        // Save the user once. Cascade will save the cart and wishlist.
        return userRepository.save(user);
    }



    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        // Only update password if a new one is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        user.setRole(updatedUser.getRole());

        // No need to call save, the transaction will handle it
        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // In UserService.java

    public boolean canModifyUser(Long targetUserId, UserDetails currentUser) {
        // Check if the current user is an admin
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        // Otherwise, check if the current user is trying to modify their own record
        // We fetch the user by the email/username from the security context
        return this.findByEmail(currentUser.getUsername())
                .map(user -> user.getId().equals(targetUserId)) // Check if IDs match
                .orElse(false); // If user not found, cannot modify
    }


}