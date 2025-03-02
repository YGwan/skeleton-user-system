package com.skeleton.code.user.dto.request;

import com.skeleton.code.user.domain.UserEntity;

public record SignupRequest(
    String username,
    String password,
    String email,
    String name
) {
    public UserEntity toEntity() {
        return new UserEntity(
            username,
            password,
            email,
            name
        );
    }
}
