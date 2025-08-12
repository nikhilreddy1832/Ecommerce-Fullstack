package com.ecom.productCatalog.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // "user" is reserved in PostgreSQL
public class User {

    public enum Role {
        BUYER, SELLER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WishList wishlist;

    public void setCart(Cart cart) {
        if (cart == null) {
            if (this.cart != null) {
                this.cart.setUser(null);
            }
        } else {
            cart.setUser(this);
        }
        this.cart = cart;
    }

    public void setWishlist(WishList wishlist) {
        if (wishlist == null) {
            if (this.wishlist != null) {
                this.wishlist.setUser(null);
            }
        } else {
            wishlist.setUser(this);
        }
        this.wishlist = wishlist;
    }
}
