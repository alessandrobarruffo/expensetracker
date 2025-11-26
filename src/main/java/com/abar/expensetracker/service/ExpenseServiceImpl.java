package com.abar.expensetracker.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.abar.expensetracker.dto.request.CreateExpenseRequest;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.repository.ExpenseRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;
    private CategoryService categoryService;
    private UserService userService;

    @Override
    public void save(CreateExpenseRequest expenseDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Expense expense = new Expense();
        expense.setPrice(expenseDto.getPrice());
        expense.setCategory(categoryService.getById(expenseDto.getCategory()));
        expense.setUser(userService.findByUsername(username));
        expenseRepository.save(expense);
    }

}
