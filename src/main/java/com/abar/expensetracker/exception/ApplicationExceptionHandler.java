package com.abar.expensetracker.exception;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler  {

@ExceptionHandler(EntityNotFoundException.class)
 public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(Exception exception) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), Arrays.asList(exception.getMessage()));
    return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
 }

}
