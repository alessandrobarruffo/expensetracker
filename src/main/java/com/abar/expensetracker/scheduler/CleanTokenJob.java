package com.abar.expensetracker.scheduler;

import com.abar.expensetracker.repository.RefreshTokenRepository;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CleanTokenJob {

  private final RefreshTokenRepository refreshTokenRepository;

  @EventListener(ApplicationReadyEvent.class)
  public void onStart() {
    this.clean();
  }

  public void clean() {
    this.refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
  }
}
