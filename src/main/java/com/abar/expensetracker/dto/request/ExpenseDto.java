package com.abar.expensetracker.dto.request;

import java.math.BigDecimal;

import com.abar.expensetracker.validation.OnCreate;

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
    

}
