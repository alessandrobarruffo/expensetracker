package com.abar.expensetracker.security.manager;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.exception.EntityNotFoundException;
import com.abar.expensetracker.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user;
    try {
      user = userService.findByUsername(authentication.getPrincipal().toString());
    } catch (EntityNotFoundException e) {
      throw new BadCredentialsException("Wrong credentials");
    }
    if (bCryptPasswordEncoder.matches(
        authentication.getCredentials().toString(), user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user.getUsername(), null, List.of());
    } else {
      throw new BadCredentialsException("Wrong credentials");
    }
  }
}
