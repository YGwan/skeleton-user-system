package com.skeleton.code.user.dto.response;

import com.skeleton.code.user.domain.UserEntity;

public record UserResponse(
    String username,
    String email,
    String name
) {
    public static  UserResponse of(UserEntity entity) {
        return new UserResponse(
            entity.getUsername(),
            entity.getEmail(),
            entity.getName()
        );
    }
}
