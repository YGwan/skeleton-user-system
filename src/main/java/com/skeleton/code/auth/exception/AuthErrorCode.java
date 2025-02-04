package com.skeleton.code.auth.exception;

import com.skeleton.code.common.exception.HttpErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements HttpErrorCode {
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "user not exists"),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "access denied"),
    ;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
