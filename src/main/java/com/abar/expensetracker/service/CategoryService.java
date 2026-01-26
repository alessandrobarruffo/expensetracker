package com.abar.expensetracker.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.abar.expensetracker.dto.request.PageDto;
import com.abar.expensetracker.entity.Category;

public interface CategoryService {

    List<Category> getAll();

    PageDto<Category> getAll(Pageable pageable);

    void save(Category category);

    Category getById(Long id);

}
