package com.backend.vastrarent.model.enums;

public enum PaymentStatus {
    PENDING,           // Payment awaiting processing
    PAID,              // Payment completed
    REFUNDED,          // Payment refunded
    PARTIAL_REFUND,    // Partial payment refunded
    FAILED             // Payment processing failed
}
