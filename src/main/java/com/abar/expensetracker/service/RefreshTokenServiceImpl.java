package com.abar.expensetracker.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abar.expensetracker.entity.RefreshToken;
import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    // Refresh token duration (e.g., 7 days)
    private final long refreshTokenDurationMs = 7L * 24 * 60 * 60 * 1000;

    @Override
    public String createRefreshToken(String username) {
        User user = userService.findByUsername(username);

        // Generate a random plain token
        String rawToken = UUID.randomUUID().toString();

        // Hash the token before storing it
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        // Return the raw token to the user
        return rawToken;
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing refresh token", e);
        }
    }

}
