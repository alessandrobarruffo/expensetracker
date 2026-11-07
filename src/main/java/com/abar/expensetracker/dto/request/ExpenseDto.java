package com.abar.expensetracker.dto.request;

import com.abar.expensetracker.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.NonNull;

public record ExpenseDto(
    @NonNull @NotNull(groups = OnCreate.class) BigDecimal price,
    @NonNull @NotNull(groups = OnCreate.class) Long category,
    @Nonnull
        @NotNull(groups = OnCreate.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonProperty("expense_date")
        LocalDate expenseDate) {}
