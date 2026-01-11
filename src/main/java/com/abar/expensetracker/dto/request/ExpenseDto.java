package com.abar.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.abar.expensetracker.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExpenseDto {

    @NonNull
    @NotNull(groups = OnCreate.class)
    private BigDecimal price;

    @NonNull
    @NotNull(groups = OnCreate.class)
    private Long category;

    @Nonnull
    @NotNull(groups = OnCreate.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("expense_date")
    private LocalDate expenseDate;
    

}
