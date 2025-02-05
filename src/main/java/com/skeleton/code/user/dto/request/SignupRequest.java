package com.skeleton.code.user.dto.request;

import com.skeleton.code.user.domain.UserEntity;

public record SignupRequest(
    String email,
    String password,
    String name
) {
    public UserEntity toEntity() {
        return new UserEntity(
            email,
            password,
            name
        );
    }
}
