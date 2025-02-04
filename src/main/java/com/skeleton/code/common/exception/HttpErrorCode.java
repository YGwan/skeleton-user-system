package com.skeleton.code.common.exception;

import org.springframework.http.HttpStatus;

public interface HttpErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}