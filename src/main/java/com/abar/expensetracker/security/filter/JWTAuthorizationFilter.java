package com.abar.expensetracker.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abar.expensetracker.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.BEARER)) {
            String token = authorizationHeader.replace(SecurityConstants.BEARER, "");
           String username = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getSubject();
           Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Arrays.asList());
           SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

}
