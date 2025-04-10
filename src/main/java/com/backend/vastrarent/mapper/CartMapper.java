package com.backend.vastrarent.mapper;

import com.backend.vastrarent.dto.CartDto.*;
import com.backend.vastrarent.dto.CartItemDto;
import com.backend.vastrarent.model.Cart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    private final CartItemMapper cartItemMapper;
    public CartMapper(CartItemMapper cartItemMapper){
        this.cartItemMapper=cartItemMapper;
    }

    public CartDTO toDto(Cart cart){
        if (cart==null){
            return null;
        }
        List<CartItemDto.CartItemDTO> cartItemDTOS= cart.getItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());

        return CartDTO.builder()
                .id(cart.getId())
                .ownerId(cart.getOwner()!=null ? cart.getOwner().getId():null)
                .totalAmount(cart.getTotalAmount())
                .discountAmount(cart.getDiscountAmount())
                .finalPrice(cart.getFinalPrice())
                .couponCode(cart.getCouponCode())
                .items(cartItemDTOS)
                .build();
    }
}
