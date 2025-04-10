package com.backend.vastrarent.model;

import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private Role role;

    /**
     * Static method to convert User entity into UserPrincipal.
     */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    /**
     * Spring Security needs this to know user roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Using email as username for authentication.
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can add custom logic later
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can add custom logic later
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add custom logic later
    }

    @Override
    public boolean isEnabled() {
        return true; // You can link this with user.isActive()
    }
}
