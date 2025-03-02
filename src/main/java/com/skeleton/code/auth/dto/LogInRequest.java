package com.skeleton.code.auth.dto;

public record LogInRequest(
    String username,
    String password
) {
}
