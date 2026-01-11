package com.abar.expensetracker.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilter {
    private LocalDate from;
    private LocalDate to;
    private String categoryName;
}
