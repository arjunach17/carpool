package com.carpooling.exception;

/**
 * Thrown when booking operation fails due to business rules
 * e.g., no available seats, duplicate booking, etc.
 */
public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
