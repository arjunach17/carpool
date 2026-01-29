package com.carpooling.service.impl;

import com.carpooling.dto.request.UserRegisterRequest;
import com.carpooling.dto.response.UserResponse;
import com.carpooling.entity.User;
import com.carpooling.entity.enums.UserRole;
import com.carpooling.exception.BookingException;
import com.carpooling.exception.ResourceNotFoundException;
import com.carpooling.repository.UserRepository;
import com.carpooling.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for user management
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user
     * Validates unique username/email, encrypts password, persists user
     */
    @Override
    public UserResponse register(UserRegisterRequest request) {

        // Validate username doesn't exist
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BookingException("Username '" + request.getUsername() + "' already exists");
        }

        // Validate email doesn't exist
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BookingException("Email '" + request.getEmail() + "' already exists");
        }

        // Validate role
        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BookingException("Invalid role. Must be DRIVER or PASSENGER");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * Retrieve user by ID
     */
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return new UserResponse(user);
    }

    /**
     * Retrieve user by username (used by Spring Security)
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
