package com.skeleton.code.auth.exception;

import com.skeleton.code.common.exception.BusinessException;
import com.skeleton.code.common.exception.HttpErrorCode;

public class AuthException extends BusinessException {
    public AuthException(HttpErrorCode httpErrorCode) {
        super(httpErrorCode);
    }
}
