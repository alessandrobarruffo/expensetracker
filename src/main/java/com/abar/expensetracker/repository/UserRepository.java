package com.abar.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abar.expensetracker.entity.User;
import java.util.List;


public interface  UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsername(String username);

}
