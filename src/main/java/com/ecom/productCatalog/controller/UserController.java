package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.User;
import com.ecom.productCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // This is correct for a @RestController
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (!userService.canModifyUser(id, userDetails)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        User user = userService.updateUser(id, updatedUser);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // This is also correct for a @RestController
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (!userService.canModifyUser(id, userDetails)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        userService.deleteUser(id);

        // If the line above executes without error, the deletion was successful.
        return ResponseEntity.ok("User deleted successfully.");
    }
}