package com.backend.vastrarent.model.enums;

public enum StatusCoupon {
    ACTIVE,         // Currently valid and can be used
    EXPIRED,        // Expired based on validTill
    UPCOMING,       // Not yet valid (validFrom is in future)
    USED,           // Already used by the user (if single-use)
    DISABLED,       // Manually disabled by admin
    INVALID         // Invalid or non-existent coupon
}
