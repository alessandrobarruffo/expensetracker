package com.abar.expensetracker.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abar.expensetracker.dto.request.PageDto;
import com.abar.expensetracker.entity.Category;
import com.abar.expensetracker.exception.EntityNotFoundException;
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
    public PageDto<Category> getAll(@NonNull Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        return new PageDto<>(page.getContent(), page.getTotalElements());
    }

    @Override
    public void save(@NonNull Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category getById(@NonNull Long id) {
        try {
            return categoryRepository.findById(id).get();
        } catch (Exception e) {
            throw new EntityNotFoundException(Category.class.getSimpleName(), id);

        }
    }

}
