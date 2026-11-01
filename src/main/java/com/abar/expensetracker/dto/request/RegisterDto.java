package com.abar.expensetracker.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;

public record RegisterDto(@Nonnull @NotBlank String username, @Nonnull @NotBlank String password) {}
