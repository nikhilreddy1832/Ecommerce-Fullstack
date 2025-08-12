package com.ecom.productCatalog.repository;

import com.ecom.productCatalog.model.Order;
import com.ecom.productCatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find an order by the ID provided by Razorpay
    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

    // Find all orders placed by a specific user (for order history)
    List<Order> findByUser(User user);
}