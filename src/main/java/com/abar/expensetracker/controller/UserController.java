package com.abar.expensetracker.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.security.SecurityConstants;
import com.abar.expensetracker.service.RefreshTokenService;
import com.abar.expensetracker.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API", description = "Endpoints for managing users and authentication")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system. The password will be encrypted.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody User user) {
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", description = "Logout successfully")
    @PostMapping("/authenticate/logout")
    public ResponseEntity<HttpStatus> logout(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        refreshTokenService.revokeRefreshToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/user/authenticate")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Refresh token")
    @ApiResponse(responseCode = "201", description = "Token refreshed successfully")
    @PostMapping("authenticate/refresh")
    public ResponseEntity<HttpStatus> refreshToken(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            // Verifica il refresh token e ottieni l'utente
            User user = refreshTokenService.verifyRefreshTokenAndGetUser(refreshToken);
            String username = user.getUsername();

            // Genera il nuovo Access Token
            String token = JWT.create().withSubject(username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(secretKey));

            // Genera il nuovo Refresh Token
            String newRefreshToken = refreshTokenService.createRefreshToken(username);

            ResponseCookie cookie = ResponseCookie.from("Refresh-Token", newRefreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/api/user/authenticate")
                    .maxAge(SecurityConstants.REFRESH_TOKEN_EXPIRATION)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            response.setHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            // Se il token è invalido, scaduto o revocato
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
