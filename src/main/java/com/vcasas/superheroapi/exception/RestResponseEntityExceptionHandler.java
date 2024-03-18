package com.vcasas.superheroapi.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SuperHeroeException.class)
    protected ResponseEntity<ErrorResponse> handleSuperHeroeNotFoundException(RuntimeException ex, WebRequest request) {
      ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        new Date().getTime()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}