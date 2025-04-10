package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByOwnerAndStatus(User owner, CartStatus status);
    Optional<Cart> findByOwner(User owner);
}