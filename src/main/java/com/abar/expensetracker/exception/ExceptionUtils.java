package com.abar.expensetracker.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExceptionUtils {

    private final ObjectMapper mapper;
    public static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

      public void sendErrorResponse(HttpServletResponse response, RuntimeException e, int statusCode)
            throws IOException, JsonProcessingException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        ErrorResponse errorResponse = new ErrorResponse(statusCode + "",null, LocalDateTime.now(), e.getMessage());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }

    public void sendUnexpectedErrorResponse(HttpServletResponse response, RuntimeException e) throws IOException {

          String requestId = UUID.randomUUID().toString();
          logger.error("Request id {},  Generic server error: {} ", requestId,e.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = new ErrorResponse( ErrorCode.GENERIC_ERROR.getCode(), requestId, LocalDateTime.now(), ErrorCode.GENERIC_ERROR.getDefaultMessage());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.getWriter().flush();


    }


}
