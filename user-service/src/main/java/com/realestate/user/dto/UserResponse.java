package com.realestate.user.dto;

import com.realestate.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User response DTO (excludes password)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String walletAddress;
    private Boolean emailVerified;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
