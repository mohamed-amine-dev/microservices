package com.realestate.user.repository;

import com.realestate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByWalletAddress(String walletAddress);
    
    boolean existsByEmail(String email);
    
    boolean existsByWalletAddress(String walletAddress);
}
