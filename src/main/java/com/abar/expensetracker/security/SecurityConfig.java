package com.abar.expensetracker.security;

import com.abar.expensetracker.exception.ExceptionUtils;
import com.abar.expensetracker.security.filter.AuthenticationFilter;
import com.abar.expensetracker.security.filter.ExceptionHandlerFilter;
import com.abar.expensetracker.security.filter.JWTAuthorizationFilter;
import com.abar.expensetracker.security.manager.CustomAuthenticationManager;
import com.abar.expensetracker.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationManager authenticationManager;
  private final ExceptionUtils exceptionUtils;
  private final RefreshTokenService refreshTokenService;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationFilter authenticationFilter =
        new AuthenticationFilter(
            authenticationManager, exceptionUtils, refreshTokenService, jwtSecret);
    authenticationFilter.setFilterProcessesUrl("/api/user/authenticate");
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .cors( Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests ->
            requests
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers("/h2/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH)
                .permitAll()
                .requestMatchers("/api/user/authenticate/refresh", "/api/user/authenticate/logout")
                .permitAll()
                .requestMatchers("/error")
                .permitAll()
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .addFilterBefore(new ExceptionHandlerFilter(exceptionUtils), AuthenticationFilter.class)
        .addFilter(authenticationFilter)
        .addFilterAfter(new JWTAuthorizationFilter(jwtSecret), AuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
