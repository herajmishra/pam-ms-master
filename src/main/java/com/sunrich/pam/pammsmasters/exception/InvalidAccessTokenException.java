package com.sunrich.pam.pammsmasters.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Gets thrown if auth token is missing or is invalid
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAccessTokenException extends BaseException {
    public InvalidAccessTokenException(String error, String errorDescription) {
        super(error, errorDescription);
    }
}
