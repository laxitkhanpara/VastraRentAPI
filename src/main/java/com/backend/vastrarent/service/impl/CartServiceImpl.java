package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.dto.CartDto.*;
import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.mapper.CartMapper;
import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.CartItem;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.CartStatus;
import com.backend.vastrarent.repository.CartRepository;
import com.backend.vastrarent.repository.ProductRepository;
import com.backend.vastrarent.repository.UserRepository;
import com.backend.vastrarent.service.CartItemService;
import com.backend.vastrarent.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartDTO getActiveCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Cart cart = getOrCreateCart(user);

        cartItemService.addItemToCart(cart, product, quantity);

        updateCartTotals(cart);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItemService.validateCartItemBelongsToCart(cartItem, cart);

        if (quantity <= 0) {
            cartItemService.removeCartItem(cartItem);
        } else {
            cartItemService.updateCartItem(cartItem, quantity);
        }

        updateCartTotals(cart);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO removeCartItem(Long userId, Long cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItemService.validateCartItemBelongsToCart(cartItem, cart);

        cartItemService.removeCartItem(cartItem);

        updateCartTotals(cart);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);

        cart.getItems().forEach(cartItemService::removeCartItem);
        cart.getItems().clear();

        updateCartTotals(cart);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO applyCoupon(Long userId, String couponCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);

        // Here you would validate the coupon code
        // For now, let's assume a simple 10% discount
        cart.setCouponCode(couponCode);
        cart.setDiscountAmount(cart.getTotalAmount() * 0.1);
        cart.setFinalPrice(cart.getTotalAmount() - cart.getDiscountAmount());

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO removeCoupon(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = getOrCreateCart(user);

        cart.setCouponCode(null);
        cart.setDiscountAmount(0);
        cart.setFinalPrice(cart.getTotalAmount());

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByOwnerAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .owner(user)
                            .status(CartStatus.ACTIVE)
                            .totalAmount(0.0)
                            .discountAmount(0.0)
                            .finalPrice(0.0)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private void updateCartTotals(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        cart.setTotalAmount(total);

        // Recalculate discount if coupon is applied
        if (cart.getCouponCode() != null && !cart.getCouponCode().isEmpty()) {
            // Assuming a simple 10% discount for demo purposes
            cart.setDiscountAmount(total * 0.1);
        } else {
            cart.setDiscountAmount(0);
        }
        cart.setFinalPrice(total - cart.getDiscountAmount());
    }
}