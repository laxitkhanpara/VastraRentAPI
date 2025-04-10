package com.backend.vastrarent.payload.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


// JWT Authentication Response payload
@Data
public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public JwtAuthResponse(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }
}
