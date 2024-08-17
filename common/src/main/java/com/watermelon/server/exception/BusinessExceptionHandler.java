package com.watermelon.server.exception;

import com.watermelon.server.auth.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(AuthenticationException ex) {
        log.error("AuthenticationException", ex);
        final ErrorResponse response = ErrorResponse.of(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
