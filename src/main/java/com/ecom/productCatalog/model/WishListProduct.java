package com.ecom.productCatalog.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wishlist_products")
public class WishListProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public WishListProduct(WishList wishlist, Product product) {
        this.wishlist = wishlist;
        this.product = product;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_wishlist_product_wishlist"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WishList wishlist;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_wishlist_product_product"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
