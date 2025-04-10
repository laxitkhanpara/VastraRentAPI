package com.backend.vastrarent.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Sign Up Request payload
@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}

