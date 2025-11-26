package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.dto.request.CreateExpenseRequest;
import com.abar.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/expense")
@AllArgsConstructor

public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping("")
    public ResponseEntity<HttpStatus> saveExpense(
            @Valid @RequestBody CreateExpenseRequest expenseDto) {
        expenseService.save(expenseDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
