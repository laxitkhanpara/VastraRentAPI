package com.backend.vastrarent.payload.response;
import lombok.Data;

// API Response payload
@Data
public class ApiResponse {
    private Boolean success;
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

