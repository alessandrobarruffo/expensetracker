package com.abar.expensetracker.service;

import java.util.List;

import com.abar.expensetracker.dto.request.ExpenseDto;
import com.abar.expensetracker.entity.Expense;

public interface ExpenseService {
    public void save(ExpenseDto expense);
    public List<Expense> getAllByUsername();
}
