package com.abar.expensetracker.service;

import java.util.List;

import com.abar.expensetracker.entity.Category;

public interface CategoryService {

    List<Category> getAll();
    void save(Category category);
    Category getById(Long id);

}
