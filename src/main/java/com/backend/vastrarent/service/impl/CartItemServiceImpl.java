package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.CartItem;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.repository.CartItemRepository;
import com.backend.vastrarent.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItem addItemToCart(Cart cart, Product product, Integer quantity) {
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cartItem.getUnitPrice() * cartItem.getQuantity());
            return cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getRentalPrice())
                    .totalPrice(product.getRentalPrice() * quantity)
                    .build();
            cart.getItems().add(cartItem);
            return cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public CartItem updateCartItem(CartItem cartItem, Integer quantity) {
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getUnitPrice() * quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(CartItem cartItem) {
        Cart cart = cartItem.getCart();
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findByCartAndProduct(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
    }

    @Override
    public void validateCartItemBelongsToCart(CartItem cartItem, Cart cart) {
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item does not belong to user's cart");
        }
    }
}