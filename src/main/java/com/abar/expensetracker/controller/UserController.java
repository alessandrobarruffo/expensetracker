package com.abar.expensetracker.controller;

import java.util.Date;

import com.abar.expensetracker.dto.request.RegisterDto;
import com.abar.expensetracker.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.security.SecurityConstants;
import com.abar.expensetracker.service.RefreshTokenService;
import com.abar.expensetracker.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API", description = "Endpoints for managing users and authentication")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${jwt.secret}")
    private String secretKey;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system. The password will be encrypted.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegisterDto registerDto) {
        User user = new User(registerDto.username(), registerDto.password());
        userService.register(user);
    }


    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", description = "Logout successfully")
    @PostMapping("/authenticate/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken) {
        if (refreshToken != null) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }
        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/user/authenticate")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @Operation(summary = "Refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @PostMapping("authenticate/refresh")
    public ResponseEntity<UserResponse> refreshToken(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
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
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .header(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token)
                    .body(new UserResponse(username));
        } catch (RuntimeException e) {
            // Se il token è invalido, scaduto o revocato
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
