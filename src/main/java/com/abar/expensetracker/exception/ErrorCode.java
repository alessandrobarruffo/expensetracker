package com.abar.expensetracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND("ERR_404_NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found"),
    DATA_INTEGRITY_VIOLATION("ERR_500_DATA_INTEGRITY", HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving data"),
    INVALID_PROPERTY_REFERENCE("ERR_400_INVALID_PROPERTY", HttpStatus.BAD_REQUEST, "Error in request format"),
    GENERIC_ERROR("ERR_500_GENERIC", HttpStatus.INTERNAL_SERVER_ERROR, "A server error occurred, try again later");

    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
