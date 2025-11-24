package com.abar.expensetracker.service;

import org.springframework.stereotype.Service;

import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.repository.ExpenseRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    ExpenseRepository expenseRepository;

    @Override
    public void save(Expense expense) { 
        expenseRepository.save(expense);
    }

    

}
