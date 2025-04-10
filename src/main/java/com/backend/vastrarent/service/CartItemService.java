package com.backend.vastrarent.service;

import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.CartItem;
import com.backend.vastrarent.model.Product;

import java.util.Optional;

public interface CartItemService {
    CartItem addItemToCart(Cart cart, Product product, Integer quantity);
    CartItem updateCartItem(CartItem cartItem, Integer quantity);
    void removeCartItem(CartItem cartItem);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    CartItem findById(Long cartItemId);
    void validateCartItemBelongsToCart(CartItem cartItem, Cart cart);
}