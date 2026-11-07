package com.abar.expensetracker.repository;

import com.abar.expensetracker.entity.RefreshToken;
import com.abar.expensetracker.entity.User;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByTokenHash(String tokenHash);

  List<RefreshToken> findByUser(User user);

  @Transactional
  void deleteByExpiresAtBefore(Instant now);
}
