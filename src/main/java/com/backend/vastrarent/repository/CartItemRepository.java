package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.CartItem;
import com.backend.vastrarent.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
