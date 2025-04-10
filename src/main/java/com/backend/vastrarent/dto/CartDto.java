package com.backend.vastrarent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.backend.vastrarent.dto.CartItemDto.*;
public class CartDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartDTO{
        private Long id;
        private Long ownerId;
        private double totalAmount;
        private double discountAmount;
        private double finalPrice;
        private String couponCode;
        private List<CartItemDTO> items = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AddToCartRequest {
        private Long productId;
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateCartItemRequest {
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ApplyCouponRequest {
        private String couponCode;
    }
}
