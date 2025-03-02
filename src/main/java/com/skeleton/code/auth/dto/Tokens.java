package com.skeleton.code.auth.dto;

public record Tokens(
    String accessToken,
    String refreshToken
) {
}
