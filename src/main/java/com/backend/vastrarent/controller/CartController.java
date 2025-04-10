package com.backend.vastrarent.controller;

import com.backend.vastrarent.dto.CartDto;
import com.backend.vastrarent.dto.CartItemDto;
import com.backend.vastrarent.dto.CartDto.*;
import com.backend.vastrarent.dto.CartItemDto.*;
import com.backend.vastrarent.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getActiveCartForUser(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addItemToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItemToCart(userId, request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, itemId, request.getQuantity()));
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDTO> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeCartItem(userId, itemId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CartDTO> clearCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @PostMapping("/{userId}/coupon")
    public ResponseEntity<CartDTO> applyCoupon(
            @PathVariable Long userId,
            @RequestBody ApplyCouponRequest request) {
        return ResponseEntity.ok(cartService.applyCoupon(userId, request.getCouponCode()));
    }

    @DeleteMapping("/{userId}/coupon")
    public ResponseEntity<CartDTO> removeCoupon(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.removeCoupon(userId));
    }

}