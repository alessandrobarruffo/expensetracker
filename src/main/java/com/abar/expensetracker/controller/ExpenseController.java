package com.abar.expensetracker.controller;

import com.abar.expensetracker.validation.OnCreate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.abar.expensetracker.dto.request.ExpenseDto;
import com.abar.expensetracker.dto.request.ExpenseFilter;
import com.abar.expensetracker.dto.request.PageDto;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.service.ExpenseService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Expense API", description = "Endpoints for managing user expenses")
@RestController
@RequestMapping(path = "/api/expense")
@AllArgsConstructor
public class ExpenseController {

    private ExpenseService expenseService;

    @Operation(summary = "Create an expense", description = "Saves a new expense record.")
    @ApiResponse(responseCode = "201", description = "Expense created successfully")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveExpense(
            @Validated(OnCreate.class) @RequestBody ExpenseDto expenseDto) {
        expenseService.save(expenseDto);
    }

    @Operation(summary = "Get expenses", description = "Retrieves a paginated list of expenses with optional filtering.")
    @ApiResponse(responseCode = "200", description = "Paginated list of expenses retrieved successfully")
    @GetMapping()
    public ResponseEntity<PageDto<ExpenseDto>> getExpenses(@ParameterObject ExpenseFilter filter, @ParameterObject Pageable pageable) {
        Page<Expense> expenses = expenseService.getAll(filter, pageable);
        List<ExpenseDto> expensesDto = expenses.stream()
                .map((expense) -> new ExpenseDto(expense.getPrice(), expense.getId(), expense.getExpenseDate()))
                .collect(Collectors.toList());
        PageDto<ExpenseDto> expensesDtoPage = new PageDto<>(expensesDto, expenses.getTotalElements());
        return ResponseEntity.ok(expensesDtoPage);
    }

}
