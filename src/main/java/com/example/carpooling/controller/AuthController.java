package com.carpooling.controller;

import com.carpooling.dto.request.UserRegisterRequest;
import com.carpooling.dto.request.UserLoginRequest;
import com.carpooling.dto.response.ApiResponse;
import com.carpooling.dto.response.UserResponse;
import com.carpooling.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user registration and login
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * POST /api/auth/register
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {

        UserResponse response = userService.register(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    /**
     * POST /api/auth/login
     * Authenticate user and create session
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody UserLoginRequest request) {

        try {
            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Store authentication in SecurityContext (creates session)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch and return user details
            UserResponse userResponse = userService.getUserByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .map(UserResponse::new)
                    .orElseThrow();

            return new ResponseEntity<>(
                    ApiResponse.success("Login successful", userResponse),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.error("Invalid username or password"),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    /**
     * POST /api/auth/logout
     * Logout current user
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(
                ApiResponse.success("Logout successful"),
                HttpStatus.OK
        );
    }

    /**
     * GET /api/auth/me
     * Get current logged-in user (requires authentication)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserResponse response = userService.getUserByUsername(username)
                .map(UserResponse::new)
                .orElseThrow();

        return ResponseEntity.ok(ApiResponse.success("Current user", response));
    }
}
