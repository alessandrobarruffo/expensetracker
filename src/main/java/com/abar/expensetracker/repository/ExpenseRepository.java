package com.abar.expensetracker.repository;

import com.abar.expensetracker.entity.Expense;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpenseRepository
    extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

  public List<Expense> findAll(Specification<Expense> spec);
}
