package com.backend.vastrarent.model.enums;

public enum DepositStatus {
    PENDING,           // Deposit awaiting processing
    HELD,              // Deposit held
    PARTIAL_REFUND,    // Partial deposit refunded
    FULL_REFUND,       // Full deposit refunded
    FORFEITED          // Deposit kept due to damages/violations
}