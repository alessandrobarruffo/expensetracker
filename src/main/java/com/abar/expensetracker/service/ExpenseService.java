package com.abar.expensetracker.service;

import com.abar.expensetracker.dto.request.CreateExpenseRequest;
import com.abar.expensetracker.entity.Expense;

public interface ExpenseService {

    public void save(Expense expense);


}
