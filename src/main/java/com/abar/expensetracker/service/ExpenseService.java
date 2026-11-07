package com.abar.expensetracker.service;

import com.abar.expensetracker.dto.request.ExpenseDto;
import com.abar.expensetracker.dto.request.ExpenseFilter;
import com.abar.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {
  public void save(ExpenseDto expense);

  public Page<Expense> getAll(ExpenseFilter filter, Pageable pageables);
}
