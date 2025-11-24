package com.abar.expensetracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.abar.expensetracker.exception.ExceptionUtils;
import com.abar.expensetracker.security.filter.AuthenticationFilter;
import com.abar.expensetracker.security.filter.ExceptionHandlerFilter;
import com.abar.expensetracker.security.filter.JWTAuthorizationFilter;
import com.abar.expensetracker.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;


@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationManager authenticationManager;
    private final ExceptionUtils exceptionUtils;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, exceptionUtils);
        authenticationFilter.setFilterProcessesUrl("/user/authenticate");
        http
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/h2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                        .anyRequest().authenticated()

                ).addFilterBefore(new ExceptionHandlerFilter(exceptionUtils), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

}
