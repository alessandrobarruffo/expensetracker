package com.abar.expensetracker.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateExpenseRequest {

    @NonNull
    @NotNull
    private BigDecimal price;

    @NonNull
    @NotNull
    private Long category;
    

}
