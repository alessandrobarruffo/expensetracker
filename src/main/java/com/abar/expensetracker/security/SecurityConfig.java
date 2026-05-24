package com.abar.expensetracker.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.abar.expensetracker.exception.ExceptionUtils;
import com.abar.expensetracker.security.filter.AuthenticationFilter;
import com.abar.expensetracker.security.filter.ExceptionHandlerFilter;
import com.abar.expensetracker.security.filter.JWTAuthorizationFilter;
import com.abar.expensetracker.security.manager.CustomAuthenticationManager;
import com.abar.expensetracker.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, exceptionUtils,
                refreshTokenService, jwtSecret);
        authenticationFilter.setFilterProcessesUrl("/user/authenticate");
        http
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/h2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/swagger-ui.html",
                                "/swagger-resources/**", "/webjars/**")
                        .permitAll()
                        .anyRequest().authenticated()

                ).addFilterBefore(new ExceptionHandlerFilter(exceptionUtils), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(jwtSecret), AuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh-Token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**");
    }

}
