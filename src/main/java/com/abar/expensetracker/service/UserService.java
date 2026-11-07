package com.abar.expensetracker.service;

import com.abar.expensetracker.entity.User;

public interface UserService {

  void register(User user);

  User findByUsername(String username);
}
