package com.backend.vastrarent.mapper;


import com.backend.vastrarent.dto.CartItemDto.*;
import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDTO toDto(CartItem cartItem){
            if (cartItem==null){
                return null;
            }

        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct() != null ? cartItem.getProduct().getId() : null)
                .productName(cartItem.getProduct() != null ? cartItem.getProduct().getTitle() : null)
                .productImage(cartItem.getProduct() != null ? cartItem.getProduct().getImageUrls().get(0) : null)
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}
