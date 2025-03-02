package com.skeleton.code.user.dto.response;

import com.skeleton.code.user.domain.UserEntity;

public record SignupResponse(
    String username,
    String password,
    String email,
    String name
) {
    public static SignupResponse of(UserEntity entity) {
        return new SignupResponse(
            entity.getUsername(),
            entity.getPassword(),
            entity.getEmail(),
            entity.getName()
        );
    }
}
