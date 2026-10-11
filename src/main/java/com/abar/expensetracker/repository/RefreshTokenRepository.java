package com.abar.expensetracker.repository;

import com.abar.expensetracker.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.abar.expensetracker.entity.RefreshToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findByUser(User user);
    @Transactional void deleteByExpiresAtBefore(Instant now);
    // TODO: add index to tokenHash column
}
