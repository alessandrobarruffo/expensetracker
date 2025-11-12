package com.abar.expensetracker.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.abar.expensetracker.entity.Category;



public interface CategoryRepository extends JpaRepository<Category, Long>{

}
