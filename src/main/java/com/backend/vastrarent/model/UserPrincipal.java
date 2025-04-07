package com.backend.vastrarent.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

package com.backend.vastrarent.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * This class acts as a bridge between our application's User entity and Spring Security's user representation.
 * In this implementation, email is used as the username for authentication purposes.
 */
public class UserPrincipal implements UserDetails {

    // Custom fields not in the standard UserDetails interface
    private Long id;               // Database ID of the user
    private String name;           // Full name of the user
    private String email;          // Email address of the user (used as username)
    private String password;       // Encoded password (required by UserDetails)
    private Collection<? extends GrantedAuthority> authorities;  // User roles and permissions (required by UserDetails)

    /**
     * Constructor that initializes all fields.
     *
     * @param id User's unique identifier
     * @param name User's full name
     * @param email User's email address (used as username)
     * @param password User's encoded password
     * @param authorities Collection of authorities/roles granted to the user
     */
    public UserPrincipal(Long id, String name, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Static factory method to create a UserPrincipal instance from a User entity.
     * This converts domain-specific User objects to Spring Security's UserDetails objects.
     *
     * @param user The domain User entity from which to create the UserPrincipal
     * @return A new UserPrincipal instance representing the given User
     */
    public static UserPrincipal create(User user) {
        // Convert each role to a SimpleGrantedAuthority
        // Spring Security uses these authorities to check if a user has permission to access resources
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // Create and return a new UserPrincipal with all the user details
        // Note: Email is used instead of username
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    /**
     * Gets the user ID.
     * This is a custom method not required by UserDetails interface.
     *
     * @return The user's unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user's full name.
     * This is a custom method not required by UserDetails interface.
     *
     * @return The user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's email address.
     * This is a custom method not required by UserDetails interface.
     *
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the authorities granted to the user.
     * Required by UserDetails interface.
     *
     * @return Collection of authorities (roles/permissions)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Gets the user's password.
     * Required by UserDetails interface.
     *
     * @return The user's encoded password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username used to authenticate the user.
     * In this implementation, we use email as the username.
     * Required by UserDetails interface.
     *
     * @return The user's email (used as username)
     */
    @Override
    public String getUsername() {
        return email;  // Using email as the username
    }

    /**
     * Indicates whether the user's account has expired.
     * Required by UserDetails interface.
     *
     * @return true if the account is not expired (always returns true in this implementation)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;  // Account never expires in this implementation
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Required by UserDetails interface.
     *
     * @return true if the account is not locked (always returns true in this implementation)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account is never locked in this implementation
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * Required by UserDetails interface.
     *
     * @return true if credentials are valid (always returns true in this implementation)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credentials never expire in this implementation
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Required by UserDetails interface.
     *
     * @return true if the user is enabled (always returns true in this implementation)
     */
    @Override
    public boolean isEnabled() {
        return true;  // User is always enabled in this implementation
    }
}