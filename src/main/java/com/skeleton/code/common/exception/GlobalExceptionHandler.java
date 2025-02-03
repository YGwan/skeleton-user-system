package com.skeleton.code.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(Exception e) {
        return ResponseEntity.status(FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unhandledException(Exception e) {
        e.printStackTrace();
        log.error("[SERV_ERR] {}", e.getMessage());
        return ResponseEntity.internalServerError().body("Unhandled exception");
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestPartException.class,
    })
    public ResponseEntity<String> methodArgumentTypeMismatchException(Exception e) {
        return ResponseEntity.badRequest().body("Invalid API request: [path variable or query parameter type mismatch]");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> wrongRequestMethod(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.badRequest().body("Invalid HTTP method: [HTTP METHOD]");
    }
}

