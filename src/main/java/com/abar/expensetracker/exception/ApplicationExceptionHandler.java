package com.abar.expensetracker.exception;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  public static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(Exception exception) {
    return ResponseEntity.status(ErrorCode.ENTITY_NOT_FOUND.getHttpStatus())
        .body(
            new ErrorResponse(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                null,
                LocalDateTime.now(),
                ErrorCode.ENTITY_NOT_FOUND.getDefaultMessage()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> dataIntegrityViolationExceptionHandler(Exception exception) {
    String requestId = UUID.randomUUID().toString();

    if (exception.getCause() instanceof ConstraintViolationException
        && "users.username"
            .equalsIgnoreCase(
                ((ConstraintViolationException) exception.getCause()).getConstraintName())) {
      return ResponseEntity.status(ErrorCode.USERNAME_TAKEN.getHttpStatus())
          .body(
              new ErrorResponse(
                  ErrorCode.USERNAME_TAKEN.getCode(),
                  null,
                  LocalDateTime.now(),
                  ErrorCode.USERNAME_TAKEN.getDefaultMessage()));
    }
    logger.error(
        "Request id {}, - data integrity violation: {}",
        requestId,
        exception.getMessage(),
        exception);

    return ResponseEntity.status(ErrorCode.DATA_INTEGRITY_VIOLATION.getHttpStatus())
        .body(
            new ErrorResponse(
                ErrorCode.DATA_INTEGRITY_VIOLATION.getCode(),
                requestId,
                LocalDateTime.now(),
                ErrorCode.DATA_INTEGRITY_VIOLATION.getDefaultMessage()));
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ErrorResponse> propertyReferenceExceptionHandler(Exception exception) {
    return ResponseEntity.status(ErrorCode.INVALID_PROPERTY_REFERENCE.getHttpStatus())
        .body(
            new ErrorResponse(
                ErrorCode.INVALID_PROPERTY_REFERENCE.getCode(),
                null,
                LocalDateTime.now(),
                ErrorCode.INVALID_PROPERTY_REFERENCE.getDefaultMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception exception) {
    String requestId = UUID.randomUUID().toString();

    logger.error("RequestId: {} - Generic error:", requestId, exception);

    return ResponseEntity.status(ErrorCode.GENERIC_ERROR.getHttpStatus())
        .body(
            new ErrorResponse(
                ErrorCode.GENERIC_ERROR.getCode(),
                requestId,
                LocalDateTime.now(),
                ErrorCode.GENERIC_ERROR.getDefaultMessage()));
  }
}
