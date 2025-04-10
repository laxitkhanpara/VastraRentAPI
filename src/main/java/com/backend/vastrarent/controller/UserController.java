package com.backend.vastrarent.controller;

import com.backend.vastrarent.dto.UserDto;
import com.backend.vastrarent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserDto.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto.UserResponse> createUser(@Valid @RequestBody UserDto.UserRegistrationRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto.UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<UserDto.UserResponse> updateProfilePicture(
            @PathVariable Long id,
            @RequestBody String profilePictureUrl) {
        return ResponseEntity.ok(userService.updateProfilePicture(id, profilePictureUrl));
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<UserDto.UserResponse> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.PasswordChangeRequest request) {
        return ResponseEntity.ok(userService.changePassword(id, request));
    }
}