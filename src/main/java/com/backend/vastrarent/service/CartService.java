package com.backend.vastrarent.service;

import com.backend.vastrarent.dto.CartDto.*;
import com.backend.vastrarent.dto.CartItemDto;

public interface CartService {
    CartDTO getActiveCartForUser(Long userId);
    CartDTO addItemToCart(Long userId, Long productId, Integer quantity);
    CartDTO updateCartItem(Long userId, Long cartItemId, Integer quantity);
    CartDTO removeCartItem(Long userId, Long cartItemId);
    CartDTO clearCart(Long userId);
    CartDTO applyCoupon(Long userId, String couponCode);
    CartDTO removeCoupon(Long userId);
}