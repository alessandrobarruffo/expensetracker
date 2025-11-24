package com.abar.expensetracker.service;

import com.abar.expensetracker.entity.User;

public interface UserService {

    public void register(User user);
    public User findByUsername(String username);

}
