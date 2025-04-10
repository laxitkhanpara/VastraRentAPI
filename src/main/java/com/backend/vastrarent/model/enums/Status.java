package com.backend.vastrarent.model.enums;

public enum Status {
    AVAILABLE,        // Product is available for rent
    RESERVED,         // Product is temporarily held for someone (e.g., in cart or in process)
    RENTED,           // Currently rented by someone
    RETURNED,         // Returned by the renter, pending inspection or finalization
    INACTIVE,         // Hidden from listing, soft-deleted, or temporarily removed
    UNAVAILABLE,      // Not available due to maintenance, quality issues, etc.
    UNDER_REVIEW,     // Awaiting admin/user verification before it goes live
    BANNED            // Removed due to policy violations or user misuse
}
