// in com.ecom.productCatalog.service.OrderService.java
package com.ecom.productCatalog.service;

import com.ecom.productCatalog.model.Order;
import com.ecom.productCatalog.repository.OrderRepository;
import com.ecom.productCatalog.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public com.razorpay.Order createRazorpayOrder(BigDecimal amount, Long userId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();
        // Amount should be in the smallest currency unit (e.g., paise for INR)
        orderRequest.put("amount", amount.multiply(new BigDecimal(100)).intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        // Save the order to your own database with CREATED status
        Order myOrder = new Order();
        myOrder.setAmount(amount);
        myOrder.setRazorpayOrderId(razorpayOrder.get("id"));
        myOrder.setStatus(Order.OrderStatus.CREATED);
        myOrder.setUser(userRepository.findById(userId).orElseThrow()); // Get current user
        myOrder.setOrderDate(LocalDateTime.now());
        orderRepository.save(myOrder);

        return razorpayOrder;
    }
}