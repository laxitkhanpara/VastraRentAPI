package com.backend.vastrarent.dto;

import com.backend.vastrarent.model.enums.DiscountType;
import com.backend.vastrarent.model.enums.StatusCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CouponDiscountRequest {
        private String description;
        private DiscountType discountType;

        // For FLATAMOUNT type
        private BigDecimal amount;

        // For PERCENT type
        private Double percent;

        // For BUYGET type
        private Integer buy;
        private Integer get;

        private String coupon;
        private StatusCoupon statusCoupon;
        private LocalDateTime validFrom;
        private LocalDateTime validTill;
        private Long userId;
        private Long productId;
    }

    // Response DTO for returning coupon information
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CouponDiscountResponse {
        private Long id;
        private String description;
        private DiscountType discountType;
        private BigDecimal amount;
        private Double percent;
        private Integer buy;
        private Integer get;
        private String coupon;
        private StatusCoupon statusCoupon;
        private LocalDateTime validFrom;
        private LocalDateTime validTill;
        private Long userId;
        private Long productId;
    }
}
