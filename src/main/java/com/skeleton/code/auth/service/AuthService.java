package com.skeleton.code.auth.service;

import com.skeleton.code.auth.dto.Tokens;
import com.skeleton.code.auth.exception.AuthErrorCode;
import com.skeleton.code.auth.exception.AuthException;
import com.skeleton.code.auth.utils.JwtUtils;
import com.skeleton.code.user.domain.UserEntity;
import com.skeleton.code.user.domain.UserRepository;
import com.skeleton.code.user.domain.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.skeleton.code.auth.caches.Caches.*;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final Integer ACCESS_TOKEN_EXPIRED_TIME = 30 * 60 * 1000;
    private static final Integer REFRESH_TOKEN_EXPIRED_TIME = 14 * 24 * 60 * 60 * 1000;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public Tokens createToken(Authentication authentication) {
        var user = getUserByUsername(authentication.getName());
        var accessToken = createAccessToken(user);
        var refreshToken = createRefreshToken(user.getUsername());
        REFRESH_TOKENS.put(user.getUsername(), refreshToken);
        return new Tokens(accessToken, refreshToken);
    }

    public void blockToken(String token, String username) {
        BLOCKED_TOKENS.put(token, username);
    }

    public void blockUser(Long userId) {
        var user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        BLOCKED_USERNAMES.put(user.getUsername(), "");
        var refreshToken = REFRESH_TOKENS.getIfPresent(user.getUsername());
        if (refreshToken != null) {
            REFRESH_TOKENS.invalidate(refreshToken);
        }
    }

    public boolean isBlockedToken(String token) {
        return BLOCKED_TOKENS.getIfPresent(token) != null;
    }

    public boolean isBlockedUser(String username) {
        return BLOCKED_USERNAMES.getIfPresent(username) != null;
    }

    public Tokens reissue(String refreshToken) {
        var username = JwtUtils.getUsernameFromToken(secretKey, refreshToken);
        validateRefreshToken(refreshToken, username);

        var user = getUserByUsername(username);
        var newAccessToken = createAccessToken(user);
        var newRefreshToken = createRefreshToken(username);
        REFRESH_TOKENS.put(username, newRefreshToken);
        return new Tokens(newAccessToken, newRefreshToken);
    }

    private String createAccessToken(UserEntity user) {
        return JwtUtils.generate(
            secretKey,
            Map.of(
                "username", user.getUsername(),
                "email", user.getEmail()
            ),
            ACCESS_TOKEN_EXPIRED_TIME
        );
    }

    private String createRefreshToken(String username) {
        return JwtUtils.generate(
            secretKey,
            Map.of(
                "username", username
            ),
            REFRESH_TOKEN_EXPIRED_TIME
        );
    }

    private void validateRefreshToken(String refreshToken, String username) {
        var savedUserRefreshToken = REFRESH_TOKENS.getIfPresent(username);

        if (!Objects.equals(savedUserRefreshToken, refreshToken)) {
            BLOCKED_TOKENS.put(refreshToken, username);
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }
    }

    public List<String> getRoleNamesByUsername(String username) {
        var user = getUserByUsername(username);
        return getRoleNamesByUserId(user.getId());
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
    }

    private List<String> getRoleNamesByUserId(Long userId) {
        return userRoleRepository.findAllByUserId(userId).stream()
            .map(it -> it.getRole().name())
            .toList();
    }
}

