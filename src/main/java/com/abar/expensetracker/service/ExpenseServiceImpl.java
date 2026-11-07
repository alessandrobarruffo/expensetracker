package com.abar.expensetracker.service;

import com.abar.expensetracker.dto.request.ExpenseDto;
import com.abar.expensetracker.dto.request.ExpenseFilter;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.repository.ExpenseRepository;
import com.abar.expensetracker.repository.ExpenseSpecifications;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

  private ExpenseRepository expenseRepository;
  private CategoryService categoryService;
  private UserService userService;

  @Override
  public void save(ExpenseDto expenseDto) {
    String username =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    Expense expense = new Expense();
    expense.setPrice(expenseDto.price());
    expense.setCategory(categoryService.getById(expenseDto.category()));
    expense.setUser(userService.findByUsername(username));
    expense.setExpenseDate(expenseDto.expenseDate());
    expenseRepository.save(expense);
  }

  @Override
  public Page<Expense> getAll(ExpenseFilter filter, @NonNull Pageable pageable) {
    String username =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return expenseRepository.findAll(
        ExpenseSpecifications.hasUsername(username).and(ExpenseSpecifications.filter(filter)),
        pageable);
  }
}
