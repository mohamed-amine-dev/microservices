package com.realestate.user.controller;

import com.realestate.common.dto.UserResponse;
import com.realestate.common.dto.ResponseWrapper;
import com.realestate.user.dto.*;
import com.realestate.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User controller - REST API endpoints for user management
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for user authentication and management")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<ResponseWrapper<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success("User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ResponseWrapper<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ResponseWrapper.success("Login successful", response));
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get user profile by ID")
    public ResponseEntity<ResponseWrapper<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ResponseWrapper.success(user));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    public ResponseEntity<ResponseWrapper<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ResponseWrapper.success(user));
    }

    @GetMapping("/wallet/{walletAddress}")
    @Operation(summary = "Get user by wallet address")
    public ResponseEntity<ResponseWrapper<UserResponse>> getUserByWalletAddress(@PathVariable String walletAddress) {
        UserResponse user = userService.getUserByWalletAddress(walletAddress);
        return ResponseEntity.ok(ResponseWrapper.success(user));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<ResponseWrapper<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ResponseWrapper.success(users));
    }

    @PutMapping("/profile/{id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ResponseWrapper<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ResponseWrapper.success("User updated successfully", user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseWrapper<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ResponseWrapper.success("User deleted successfully"));
    }

    @PostMapping("/verify-email/{userId}")
    @Operation(summary = "Verify user email")
    public ResponseEntity<ResponseWrapper<Void>> verifyEmail(@PathVariable Long userId) {
        userService.verifyEmail(userId);
        return ResponseEntity.ok(ResponseWrapper.success("Email verified successfully"));
    }
}
