package com.abar.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abar.expensetracker.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
