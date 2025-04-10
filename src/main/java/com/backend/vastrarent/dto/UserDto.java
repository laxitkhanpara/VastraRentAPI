package com.backend.vastrarent.dto;


import com.backend.vastrarent.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRegistrationRequest {
        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
        private String phoneNumber;

        @NotBlank(message = "Password is required")
        private String password;

        private Gender gender;
        private Role role;

        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        private String preferences;
        private String profilePicture;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateRequest {
        private String fullName;
        private String email;
        private String phoneNumber;
        private Gender gender;
        private LocalDate dateOfBirth;
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        private String preferences;
        private Boolean active;
        private String profilePicture;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserResponse {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String profilePicture;
        private Gender gender;
        private LocalDate dateOfBirth;
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        private Role role;
        private boolean verified;
        private BigDecimal walletBalance;
        private BigDecimal ratings;
        private boolean active;
        private String preferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PasswordChangeRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        private String newPassword;
    }
}