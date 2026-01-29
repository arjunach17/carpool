package com.carpooling.exception;

import com.carpooling.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all controllers
 * Catches exceptions and returns consistent error responses
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(
                ApiResponse.error("Validation failed", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle constraint violation errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        String message = ex.getMessage();
        return new ResponseEntity<>(
                ApiResponse.error("Constraint violation: " + message),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle resource not found errors
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle authorization errors
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorized(
            UnauthorizedException ex, WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    /**
     * Handle booking-specific errors
     */
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApiResponse<String>> handleBookingException(
            BookingException ex, WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    /**
     * Handle bad credentials (login failure)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.error("Invalid username or password"),
                HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(
            Exception ex, WebRequest request) {

        return new ResponseEntity<>(
                ApiResponse.error("An unexpected error occurred: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
