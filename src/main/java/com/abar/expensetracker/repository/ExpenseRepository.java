package com.abar.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abar.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
