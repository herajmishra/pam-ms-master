package com.sunrich.pam.pammsmasters.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * To customize Spring's default error response
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDetails {

    private HttpStatus status;
    private String message;
    private Map<String, String> fieldErrors;
    private List<String> errors;

    public ErrorDetails(HttpStatus status, String message, List<String> errors, Map<String, String> fieldErrors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.fieldErrors = fieldErrors;
    }

    public ErrorDetails(HttpStatus status, String message, String error, Map<String, String> fieldErrors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
        this.fieldErrors = fieldErrors;
    }
}
