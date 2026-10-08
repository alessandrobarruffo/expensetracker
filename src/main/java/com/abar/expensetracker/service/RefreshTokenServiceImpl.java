package com.abar.expensetracker.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abar.expensetracker.entity.RefreshTokenStatus;
import org.springframework.stereotype.Service;

import com.abar.expensetracker.entity.RefreshToken;
import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.repository.RefreshTokenRepository;
import com.abar.expensetracker.security.SecurityConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

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
        refreshToken.setExpiresAt(Instant.now().plusMillis(SecurityConstants.REFRESH_TOKEN_EXPIRATION * 1000));

        refreshTokenRepository.save(refreshToken);

        // Return the raw token to the user
        return rawToken;
    }

    @Override
    public User verifyRefreshTokenAndGetUser(String rawToken) {
        String tokenHash = hashToken(rawToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Refresh token non valido"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token scaduto. Effettua nuovamente il login.");
        }

        if (refreshToken.getStatus().equals(RefreshTokenStatus.REVOKED)) {
            throw new RuntimeException("Refresh token revocato.");
        }

        if (refreshToken.getStatus().equals(RefreshTokenStatus.USED)) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findByUser(refreshToken.getUser());
            refreshTokens.forEach(t -> t.setStatus(RefreshTokenStatus.REVOKED));
            refreshTokenRepository.saveAll(refreshTokens);
            throw new RuntimeException("Refresh token già usato.");
        }

        refreshToken.setStatus(RefreshTokenStatus.USED);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getUser();
    }

    @Override
    public void deleteRefreshToken(String rawToken) {
        String tokenHash = hashToken(rawToken);
        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public void revokeRefreshToken(String rawToken) {
        String tokenHash = hashToken(rawToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
            token.setStatus(RefreshTokenStatus.REVOKED);
        });
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
