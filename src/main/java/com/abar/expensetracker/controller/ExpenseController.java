package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.dto.request.ExpenseDto;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.repository.ExpenseRepository;
import com.abar.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/expense")
@AllArgsConstructor

public class ExpenseController {
 
    private ExpenseService expenseService;

    @PostMapping("")
    public ResponseEntity<HttpStatus> saveExpense(
            @Valid @RequestBody ExpenseDto expenseDto) {
        expenseService.save(expenseDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<List<ExpenseDto>> getAllByUsername() {
        List<Expense> expenses = expenseService.getAllByUsername();
        List<ExpenseDto> expensesDto = expenses.stream().map((expense) -> new ExpenseDto(expense.getPrice(), expense.getId())).collect(Collectors.toList());
        return new ResponseEntity<List<ExpenseDto>>(expensesDto, HttpStatus.OK);
    }
    

}
