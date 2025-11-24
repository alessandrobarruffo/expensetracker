package com.abar.expensetracker.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.exception.EntityNotFoundException;
import com.abar.expensetracker.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(User user) {
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
      List<User> users =  userRepository.findByUsername(username);
      if (users.size() > 0) {
        return users.get(0);
      } else {
        throw new EntityNotFoundException(User.class.getSimpleName(), username);
      }
    }


}
