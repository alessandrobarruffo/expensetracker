package com.abar.expensetracker.service;    
import java.util.List;

import org.springframework.stereotype.Service;

import com.abar.expensetracker.entity.Category;
import com.abar.expensetracker.repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(@NonNull Category category) {
        categoryRepository.save(category);
    }

}
