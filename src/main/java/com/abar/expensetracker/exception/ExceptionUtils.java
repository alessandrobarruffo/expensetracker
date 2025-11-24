package com.abar.expensetracker.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExceptionUtils {

    private final ObjectMapper mapper;

      public void sendErrorResponse(HttpServletResponse response, RuntimeException e, int statusCode)
            throws IOException, JsonProcessingException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), Arrays.asList(e.getMessage()));
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }


}
