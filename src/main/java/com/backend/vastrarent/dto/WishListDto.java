package com.backend.vastrarent.dto;

import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WishListDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WishListDTO{

        private Long id;

        @NotBlank(message = "User is required")
        private User user;

        @NotBlank(message = "Product is required")
        private Product product;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WishListRequest{
        private Long id;

        @NotBlank(message = "User is required")
        private User user;

        @NotBlank(message = "Product is required")
        private Product product;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WishListResponse{
        private Long id;

        @NotBlank(message = "User is required")
        private User user;

        @NotBlank(message = "Product is required")
        private Product product;
    }
}
