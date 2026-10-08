package com.abar.expensetracker.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  public static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(Exception exception) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ENTITY_NOT_FOUND.getCode(), null,LocalDateTime.now(), exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> dataIntegrityViolationExceptionHandler(Exception exception) {
    String requestId = UUID.randomUUID().toString();
    logger.error("Request id {}, - data integrity violation: {}",requestId, exception.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.DATA_INTEGRITY_VIOLATION.getCode(), UUID.randomUUID().toString(), LocalDateTime.now(),ErrorCode.DATA_INTEGRITY_VIOLATION.getDefaultMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ErrorResponse> propertyReferenceExceptionHandler(Exception exception) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_PROPERTY_REFERENCE.getCode(),null, LocalDateTime.now(), exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception exception) {
    String requestId = UUID.randomUUID().toString();

    logger.error("RequestId: {} - Generic error:", requestId, exception);

    ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.GENERIC_ERROR.getCode(),
            requestId,
            LocalDateTime.now(),
            ErrorCode.GENERIC_ERROR.getDefaultMessage()
    );
    return new ResponseEntity<>(errorResponse, ErrorCode.GENERIC_ERROR.getHttpStatus());
  }

}
