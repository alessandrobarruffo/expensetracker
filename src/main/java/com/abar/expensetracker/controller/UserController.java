package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.security.SecurityConstants;
import com.abar.expensetracker.service.RefreshTokenService;
import com.abar.expensetracker.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "Endpoints for managing users and authentication")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private RefreshTokenService refreshTokenService;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system. The password will be encrypted.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody User user) {
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Refresh token")
    @ApiResponse(responseCode = "201", description = "Token refreshed successfully")
    @PostMapping("/refresh")
    public ResponseEntity<HttpStatus> refreshToken(
            @CookieValue(name = "refreshToken", required = true) String refreshToken, HttpServletResponse response) {
        try {
            // Verifica il refresh token e ottieni l'utente
            User user = refreshTokenService.verifyRefreshTokenAndGetUser(refreshToken);
            String username = user.getUsername();

            // eliminazione del vecchio refresh token (rotazione del token)
            refreshTokenService.deleteRefreshToken(refreshToken);

            // Genera il nuovo Access Token
            String token = JWT.create().withSubject(username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));

            // Genera il nuovo Refresh Token
            String newRefreshToken = refreshTokenService.createRefreshToken(username);
            Cookie cookie = new Cookie("Refresh-Token", newRefreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(SecurityConstants.REFRESH_TOKEN_EXPIRATION);
            response.addCookie(cookie);
            response.setHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            // Se il token è invalido, scaduto o revocato
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
