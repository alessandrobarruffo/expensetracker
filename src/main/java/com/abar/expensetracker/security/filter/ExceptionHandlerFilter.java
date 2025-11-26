package com.abar.expensetracker.security.filter;

import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abar.expensetracker.exception.ExceptionUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ExceptionUtils exceptionUtils;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            exceptionUtils.sendErrorResponse(response, e, HttpServletResponse.SC_FORBIDDEN);
        } catch (BadCredentialsException e) {
             exceptionUtils.sendErrorResponse(response, e, HttpServletResponse.SC_UNAUTHORIZED);
        }  catch (RuntimeException e) {
            exceptionUtils.sendErrorResponse(response, e, HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
