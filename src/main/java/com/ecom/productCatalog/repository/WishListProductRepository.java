package com.ecom.productCatalog.repository;

import com.ecom.productCatalog.model.Product;
import com.ecom.productCatalog.model.WishList;
import com.ecom.productCatalog.model.WishListProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListProductRepository extends JpaRepository<WishListProduct, Long> {
    List<WishListProduct> findByWishlist(WishList wishlist);
    Optional<WishListProduct> findByWishlistAndProduct(WishList wishlist, Product product);

}
