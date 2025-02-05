package com.skeleton.code.user.exception;

import com.skeleton.code.common.exception.BusinessException;
import com.skeleton.code.common.exception.HttpErrorCode;

public class UserException extends BusinessException {
    public UserException(HttpErrorCode httpErrorCode) {
        super(httpErrorCode);
    }
}
