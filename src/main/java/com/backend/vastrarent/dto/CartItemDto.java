package com.backend.vastrarent.dto;

import com.backend.vastrarent.model.Cart;
import com.backend.vastrarent.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CartItemDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemDTO{

        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemRequest{
        private Long id;

        private Long product;

        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be positive")
        private Integer quantity;

        private Double unitPrice;
        private Double totalPrice;

        private Long cart;
    }
}
