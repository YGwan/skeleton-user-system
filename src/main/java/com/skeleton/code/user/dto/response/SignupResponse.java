package com.skeleton.code.user.dto.response;

import com.skeleton.code.user.domain.UserEntity;

public record SignupResponse(
    String email,
    String name
) {
    public static SignupResponse of(UserEntity entity) {
        return new SignupResponse(
            entity.getEmail(),
            entity.getName()
        );
    }
}
