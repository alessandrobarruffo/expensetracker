package com.abar.expensetracker.dto.request;

import java.time.LocalDate;

public record ExpenseFilter(LocalDate from, LocalDate to, String categoryName) {}
