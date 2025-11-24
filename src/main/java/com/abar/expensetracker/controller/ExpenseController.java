package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.dto.request.CreateExpenseRequest;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.security.SecurityConstants;
import com.abar.expensetracker.service.CategoryService;
import com.abar.expensetracker.service.ExpenseService;
import com.abar.expensetracker.service.UserService;
import com.auth0.jwt.JWT;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping(path = "/expense")
@AllArgsConstructor

public class ExpenseController {

    private ExpenseService expenseService;
    private CategoryService categoryService;
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<HttpStatus> saveExpense(@RequestHeader(value = "Authorization", required = true)
String bearerToken, @Valid @RequestBody CreateExpenseRequest expenseDto) {
        String token = bearerToken.replace(SecurityConstants.BEARER, "");
        String username = JWT.decode(token).getSubject().toString();
        Expense expense = new Expense();
         expense.setPrice(expenseDto.getPrice());
         expense.setCategory(categoryService.getById(expenseDto.getCategory()));
         expense.setUser(userService.findByUsername(username));
         expenseService.save(expense);
    
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
