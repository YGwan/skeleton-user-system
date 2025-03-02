package com.skeleton.code.user.exception;

import com.skeleton.code.common.exception.HttpErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements HttpErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "username already exists"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
