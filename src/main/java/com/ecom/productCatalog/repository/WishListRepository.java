package com.ecom.productCatalog.repository;

import com.ecom.productCatalog.model.WishList;
import com.ecom.productCatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUser(User user);
}