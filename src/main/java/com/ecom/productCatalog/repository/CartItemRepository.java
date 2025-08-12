package com.ecom.productCatalog.repository;

import com.ecom.productCatalog.model.Cart;
import com.ecom.productCatalog.model.CartItem;
import com.ecom.productCatalog.model.Product;
import com.ecom.productCatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteAllByCart(Cart cart);


}