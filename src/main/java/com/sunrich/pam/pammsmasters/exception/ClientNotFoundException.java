package com.sunrich.pam.pammsmasters.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends NotFoundException {
    public ClientNotFoundException(String exception) {
        super(exception);
    }
}