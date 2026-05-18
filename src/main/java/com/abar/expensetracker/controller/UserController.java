package com.abar.expensetracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abar.expensetracker.entity.User;
import com.abar.expensetracker.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "Endpoints for managing users and authentication")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system. The password will be encrypted.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody User user) {
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
