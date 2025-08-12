package com.ecom.productCatalog.service;

import com.ecom.productCatalog.model.*;
import com.ecom.productCatalog.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishlistRepository;

    @Autowired
    private WishListProductRepository wishlistProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public WishList getOrCreateWishlist(User user) {
        // Re-fetch the user to ensure it's managed by the current persistence context
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(managedUser).orElseGet(() -> {
            WishList newWishlist = new WishList(managedUser);
            // This is already being done correctly, which is good!
            managedUser.setWishlist(newWishlist);
            // No need to explicitly save if the calling method is @Transactional
            // and the User entity will cascade the save. However, saving here is also fine.
            return wishlistRepository.save(newWishlist);
        });
    }

    public List<WishListProduct> getWishlistProducts(User user) {
        WishList wishlist = getOrCreateWishlist(user);
        return wishlistProductRepository.findByWishlist(wishlist);
    }

    public void addProductToWishlist(User user, Long productId) {
        WishList wishlist = getOrCreateWishlist(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean alreadyExists = wishlistProductRepository.findByWishlistAndProduct(wishlist, product).isPresent();
        if (!alreadyExists) {
            WishListProduct wishListProduct = new WishListProduct(wishlist, product);
            wishlistProductRepository.save(wishListProduct);
        }
    }

    public void removeProductFromWishlist(Long wishlistProductId) {
        wishlistProductRepository.deleteById(wishlistProductId);
    }
}
