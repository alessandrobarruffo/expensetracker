package com.abar.expensetracker.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import com.abar.expensetracker.dto.request.PageDto;
import com.abar.expensetracker.entity.Category;
import com.abar.expensetracker.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category API", description = "Endpoints for managing expense categories")
@RestController
@RequestMapping(path = "/api/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @Operation(summary = "Create a category", description = "Creates a new category to be used for expenses.")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody Category category) {
        categoryService.save(category);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a complete list of all available categories.")
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Get categories with pagination", description = "Retrieves a paginated list of categories.")
    @ApiResponse(responseCode = "200", description = "Paginated list of categories retrieved successfully")
    @GetMapping
    public ResponseEntity<PageDto<Category>> getCategories(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @Operation(summary = "Get a category by ID", description = "Retrieves the details of a specific category using its ID.")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

}
