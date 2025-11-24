package com.abar.expensetracker.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.service.UserService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    User user = userService.findByUsername(authentication.getPrincipal().toString());
    if (bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    } else {
      throw new BadCredentialsException("Wrong credentials");
    }

  }

}
