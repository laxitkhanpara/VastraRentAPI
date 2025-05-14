package com.backend.vastrarent.dto;

import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderDTO {
        private Long id;
        private String orderNumber;
        private Long renterId;
        private String renterName;
        private Long ownerId;
        private String ownerName;
        private Long productId;
        private String productTitle;
        private List<String> productImages;
        private int quantity;
        private double rentalPrice;
        private double securityDeposit;
        private double totalAmount;
        private LocalDate rentalStartDate;
        private LocalDate rentalEndDate;
        private int rentalDuration;
        private OrderStatus orderStatus;
        private PaymentStatus paymentStatus;
        private RentalStatus rentalStatus;
        private boolean isDepositReturned;
        private double depositAmountReturned;
        private String depositReturnReason;
        private LocalDateTime depositReturnDate;
        private String ownerTerms;
        private String renterTerms;
        private boolean ownerTermsAccepted;
        private boolean renterTermsAccepted;
        private List<String> damageReports = new ArrayList<>();
        private List<String> damageImages = new ArrayList<>();
        private String cancellationReason;
        private LocalDateTime cancellationDate;
        private String deliveryAddress;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryPostalCode;
        private String deliveryCountry;
        private String transactionId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Rental start date is required")
        @FutureOrPresent(message = "Rental start date must be today or in the future")
        private LocalDate rentalStartDate;

        @NotNull(message = "Rental end date is required")
        @Future(message = "Rental end date must be in the future")
        private LocalDate rentalEndDate;

        @Size(max = 500, message = "Renter terms cannot exceed 500 characters")
        private String renterTerms;

        @NotBlank(message = "Delivery address is required")
        private String deliveryAddress;

        @NotBlank(message = "Delivery city is required")
        private String deliveryCity;

        @NotBlank(message = "Delivery state is required")
        private String deliveryState;

        @NotBlank(message = "Delivery postal code is required")
        private String deliveryPostalCode;

        @NotBlank(message = "Delivery country is required")
        private String deliveryCountry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderUpdateRequest {
        private OrderStatus orderStatus;
        private PaymentStatus paymentStatus;
        private RentalStatus rentalStatus;
        private String ownerTerms;
        private Boolean ownerTermsAccepted;
        private Boolean renterTermsAccepted;
        private String cancellationReason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepositReturnRequest {
        @NotNull(message = "Return amount is required")
        @Min(value = 0, message = "Return amount cannot be negative")
        private Double returnAmount;

        @NotBlank(message = "Return reason is required")
        @Size(max = 500, message = "Return reason cannot exceed 500 characters")
        private String returnReason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DamageReportRequest {
        @NotBlank(message = "Damage description is required")
        @Size(max = 1000, message = "Damage description cannot exceed 1000 characters")
        private String description;

        private List<String> imageUrls = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderResponse {
        private String status;
        private String message;
        private Object data;
    }
}