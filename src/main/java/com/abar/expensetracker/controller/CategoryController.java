package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.dto.request.PageDto;
import com.abar.expensetracker.entity.Category;
import com.abar.expensetracker.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category API", description = "Endpoints for managing expense categories")
@RestController
@RequestMapping(path = "/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @Operation(summary = "Create a category", description = "Creates a new category to be used for expenses.")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @PostMapping
    public ResponseEntity<Category> save(@Valid @RequestBody Category category) {
        categoryService.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a complete list of all available categories.")
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get categories with pagination", description = "Retrieves a paginated list of categories.")
    @ApiResponse(responseCode = "200", description = "Paginated list of categories retrieved successfully")
    @GetMapping
    public ResponseEntity<PageDto<Category>> getCategories(Pageable pageable) {
        return new ResponseEntity<>(categoryService.getAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get a category by ID", description = "Retrieves the details of a specific category using its ID.")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

}
