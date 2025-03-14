package com.skeleton.code.auth.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class TokenUtils {

    public static Optional<String> tokenFromHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring("Bearer ".length()));
    }
}
