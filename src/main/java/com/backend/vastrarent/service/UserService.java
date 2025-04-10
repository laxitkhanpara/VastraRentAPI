package com.backend.vastrarent.service;

import com.backend.vastrarent.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService{

    @Transactional(readOnly = true)
    List<UserDto.UserResponse> getAllUsers();

    @Transactional(readOnly = true)
    UserDto.UserResponse getUserById(Long id);

    @Transactional
    UserDto.UserResponse createUser(UserDto.UserRegistrationRequest request);

    @Transactional
    UserDto.UserResponse updateUser(Long id, UserDto.UserUpdateRequest request);

    @Transactional
    void deleteUser(Long id);

    @Transactional
    UserDto.UserResponse updateProfilePicture(Long userId, String profilePictureUrl);

    @Transactional
    UserDto.UserResponse changePassword(Long userId, UserDto.PasswordChangeRequest request);
}