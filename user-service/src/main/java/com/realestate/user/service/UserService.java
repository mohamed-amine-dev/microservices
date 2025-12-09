package com.realestate.user.service;

import com.realestate.common.exception.BadRequestException;
import com.realestate.common.exception.ResourceNotFoundException;
import com.realestate.common.exception.UnauthorizedException;
import com.realestate.user.dto.*;
import com.realestate.user.entity.User;
import com.realestate.user.mapper.UserMapper;
import com.realestate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User service implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        
        // Check if wallet address already exists
        if (request.getWalletAddress() != null && 
            userRepository.existsByWalletAddress(request.getWalletAddress())) {
            throw new BadRequestException("Wallet address already registered");
        }
        
        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .walletAddress(request.getWalletAddress())
                .emailVerified(false)
                .active(true)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        // Generate JWT token
        String token = jwtService.generateToken(savedUser);
        
        return new AuthResponse(token, userMapper.toUserResponse(savedUser));
    }
    
    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        if (!user.getActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }
        
        String token = jwtService.generateToken(user);
        log.info("User logged in successfully: {}", user.getEmail());
        
        return new AuthResponse(token, userMapper.toUserResponse(user));
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Get user by wallet address
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByWalletAddress(String walletAddress) {
        User user = userRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new ResourceNotFoundException("User", "walletAddress", walletAddress));
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update user profile
     */
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user profile for ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Check email uniqueness if changed
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }
        
        // Check wallet address uniqueness if changed
        if (request.getWalletAddress() != null && !request.getWalletAddress().equals(user.getWalletAddress())) {
            if (userRepository.existsByWalletAddress(request.getWalletAddress())) {
                throw new BadRequestException("Wallet address already in use");
            }
            user.setWalletAddress(request.getWalletAddress());
        }
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User profile updated successfully: {}", updatedUser.getId());
        
        return userMapper.toUserResponse(updatedUser);
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }
    
    /**
     * Verify email
     */
    public void verifyEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setEmailVerified(true);
        userRepository.save(user);
        log.info("Email verified for user: {}", userId);
    }
}
