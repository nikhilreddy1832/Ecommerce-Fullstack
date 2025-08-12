// in com.ecom.productCatalog.model.Order.java
package com.ecom.productCatalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    public enum OrderStatus {
        CREATED, PAID, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal amount;
    private LocalDateTime orderDate;

    // You would also have a List<OrderItem> here in a full implementation
}