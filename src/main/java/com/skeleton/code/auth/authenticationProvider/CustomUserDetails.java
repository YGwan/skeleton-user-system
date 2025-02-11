package com.skeleton.code.auth.authenticationProvider;

import com.skeleton.code.user.domain.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record CustomUserDetails(
    String email,
    String password,
    List<String> roleNames
) implements UserDetails {

    public static CustomUserDetails from(UserEntity user, List<String> roleNames) {
        return new CustomUserDetails(
            user.getEmail(),
            user.getPassword(),
            roleNames
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptyList();
        }
        return roleNames.stream()
            .map(name -> "ROLE_" + name)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

