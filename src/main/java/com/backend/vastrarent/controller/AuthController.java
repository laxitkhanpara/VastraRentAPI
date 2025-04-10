package com.backend.vastrarent.controller;

import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.UserPrincipal;
import com.backend.vastrarent.model.enums.Role;
import com.backend.vastrarent.payload.response.ApiResponse;
import com.backend.vastrarent.payload.response.JwtAuthResponse;
import com.backend.vastrarent.payload.request.LoginRequest;
import com.backend.vastrarent.payload.request.SignUpRequest;
import com.backend.vastrarent.repository.UserRepository;
import com.backend.vastrarent.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract user principal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Generate JWT token
        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal.getId());

        return ResponseEntity.ok(new JwtAuthResponse(accessToken, refreshToken, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Check if email exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Check if phone number exists
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Phone number is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Create user account
        User user = User.builder()
                .fullName(signUpRequest.getFullName())
                .email(signUpRequest.getEmail())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .verified(false)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User result = userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
}