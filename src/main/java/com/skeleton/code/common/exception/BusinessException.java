package com.skeleton.code.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends IllegalArgumentException {
    private final HttpErrorCode httpErrorCode;
}