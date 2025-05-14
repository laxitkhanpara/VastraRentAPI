package com.backend.vastrarent.model.enums;

public enum OrderStatus {
    PENDING,           // Initial order creation
    AWAITING_APPROVAL, // Waiting for seller/platform approval
    APPROVED,          // Order approved by seller
    REJECTED,          // Order rejected by seller
    ACTIVE,            // Order is currently in progress
    COMPLETED,         // Order successfully finished
    CANCELLED,         // Order cancelled
    DISPUTED,
    RETURNED,           // Order under dispute
    DELIVERED,CONFIRMED
}