package com.abar.expensetracker.service;

import com.abar.expensetracker.dto.request.CreateExpenseRequest;

public interface ExpenseService {
    public void save(CreateExpenseRequest expense);
}
