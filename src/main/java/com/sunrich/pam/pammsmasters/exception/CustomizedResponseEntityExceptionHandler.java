package com.sunrich.pam.pammsmasters.exception;

import com.sunrich.pam.pammsmasters.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * To handle exceptions thrown by controllers
 */
@Slf4j
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception: " + ex);
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        List<String> errors = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + Constants.WHITE_SPACE + Constants.COLON + Constants.WHITE_SPACE + error.getDefaultMessage());
        }

        ErrorDetails apiError = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation Error", errors, fieldErrors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundException(Exception e, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, e.getMessage(), webRequest.getDescription(false), null);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error("Exception: " + ex);
        ErrorDetails apiError = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred", null);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
