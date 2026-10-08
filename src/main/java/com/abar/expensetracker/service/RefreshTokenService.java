package com.abar.expensetracker.service;

import com.abar.expensetracker.entity.User;

public interface RefreshTokenService {
    String createRefreshToken(String username);
    User verifyRefreshTokenAndGetUser(String rawToken);
    void deleteRefreshToken(String rawToken);
    void revokeRefreshToken(String rawToken);
}
