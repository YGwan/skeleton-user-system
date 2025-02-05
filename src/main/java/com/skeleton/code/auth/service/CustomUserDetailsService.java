package com.skeleton.code.auth.service;

import com.skeleton.code.auth.dto.CustomUserDetails;
import com.skeleton.code.auth.exception.AuthException;
import com.skeleton.code.user.domain.UserRepository;
import com.skeleton.code.user.domain.UserRoleEntity;
import com.skeleton.code.user.domain.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.skeleton.code.auth.exception.AuthErrorCode.ACCESS_DENIED;
import static com.skeleton.code.auth.exception.AuthErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new AuthException(USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new AuthException(ACCESS_DENIED);
        }

        var roles = userRoleRepository.findAllByUserId(user.getId()).stream()
            .map(UserRoleEntity::getRole)
            .map(Enum::name)
            .toList();

        return CustomUserDetails.from(user, roles);
    }
}

