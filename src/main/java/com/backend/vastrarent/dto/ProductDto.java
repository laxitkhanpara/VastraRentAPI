package com.backend.vastrarent.dto;

import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDTO {
        private Long id;

        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        private String title;

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
        private String description;

        @NotNull(message = "Category is required")
        private List<String> category = new ArrayList<>();

        @NotNull(message = "Size is required")
        private List<String> size = new ArrayList<>();

        @NotNull(message = "Color is required")
        private List<String> color = new ArrayList<>();

        private List<String> styleTags = new ArrayList<>();

        private String condition;

        @NotNull(message = "Retail price is required")
        @Positive(message = "Retail price must be positive")
        private Double retail;

        @NotNull(message = "Rental price is required")
        @Positive(message = "Rental price must be positive")
        private Double rentalPrice;

        @NotNull(message = "Security deposit is required")
        @Positive(message = "Security deposit must be positive")
        private Double securityDeposit;

        @NotNull(message = "Status is required")
        private Status status;

        private String careInstructions;

        private String country;
        private String state;
        private String city;
        private String postalCode;
        private String address;
        private Double latitude;
        private Double longitude;
        private boolean isAvailable = true;
        private boolean termAndCondition;
        private int views;
        private int quntity;

        @NotNull(message = "Available from date is required")
        private LocalDate availableFrom;

        @NotNull(message = "Available till date is required")
        private LocalDate availableTill;

        private List<String> imageUrls = new ArrayList<>();

        private Long ownerId;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Category is required")
        private List<String> category;

        @NotNull(message = "Size is required")
        private List<String> size;

        @NotNull(message = "Color is required")
        private List<String> color;

        private List<String> styleTags;

        private String condition;

        @NotNull(message = "Retail price is required")
        @Positive(message = "Retail price must be positive")
        private Double retail;

        @NotNull(message = "Rental price is required")
        @Positive(message = "Rental price must be positive")
        private Double rentalPrice;
        private int quntity;
        @NotNull(message = "Security deposit is required")
        @Positive(message = "Security deposit must be positive")
        private Double securityDeposit;

        private String careInstructions;

        private String country;
        private String state;
        private String city;
        private String postalCode;
        private String address;
        private Double latitude;
        private Double longitude;

        private boolean termAndCondition;

        @NotNull(message = "Available from date is required")
        private LocalDate availableFrom;

        @NotNull(message = "Available till date is required")
        private LocalDate availableTill;

        private List<String> imageUrls;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductResponse {
        private String status;
        private String message;
        private Object data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductUserDTO {
        private Long id;

        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        private String title;

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
        private String description;

        @NotNull(message = "Category is required")
        private List<String> category = new ArrayList<>();

        @NotNull(message = "Size is required")
        private List<String> size = new ArrayList<>();

        @NotNull(message = "Color is required")
        private List<String> color = new ArrayList<>();

        private List<String> styleTags = new ArrayList<>();

        private String condition;

        @NotNull(message = "Retail price is required")
        @Positive(message = "Retail price must be positive")
        private Double retail;

        @NotNull(message = "Rental price is required")
        @Positive(message = "Rental price must be positive")
        private Double rentalPrice;

        @NotNull(message = "Security deposit is required")
        @Positive(message = "Security deposit must be positive")
        private Double securityDeposit;

        @NotNull(message = "Status is required")
        private Status status;

        private String careInstructions;

        private String country;
        private String state;
        private String city;
        private String postalCode;
        private String address;
        private Double latitude;
        private Double longitude;

        private boolean isAvailable = true;
        private boolean termAndCondition;

        private int views;
        private int quntity;

        @NotNull(message = "Available from date is required")
        private LocalDate availableFrom;

        @NotNull(message = "Available till date is required")
        private LocalDate availableTill;

        private List<String> imageUrls = new ArrayList<>();

        private Long ownerId;
        private String ownerProfile;
        private String ownerName;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
