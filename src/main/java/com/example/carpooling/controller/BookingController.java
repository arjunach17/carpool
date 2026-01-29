package com.carpooling.controller;

import com.carpooling.dto.request.BookingRequest;
import com.carpooling.dto.response.ApiResponse;
import com.carpooling.dto.response.BookingResponse;
import com.carpooling.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Booking Controller
 * Handles booking operations (create, confirm, cancel, retrieve)
 */
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * POST /api/bookings
     * Book a ride (PASSENGER only)
     */
    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ApiResponse<BookingResponse>> bookRide(
            @Valid @RequestBody BookingRequest request) {

        Long passengerId = getCurrentUserId();
        BookingResponse response = bookingService.bookRide(request.getRideId(), passengerId);

        return new ResponseEntity<>(
                ApiResponse.success("Booking request created", response),
                HttpStatus.CREATED
        );
    }

    /**
     * GET /api/bookings/{bookingId}
     * Get booking details
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(
            @PathVariable Long bookingId) {

        BookingResponse response = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking found", response));
    }

    /**
     * GET /api/bookings/my-bookings
     * Get all bookings made by current passenger (PASSENGER only)
     */
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long passengerId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);

        Page<BookingResponse> response = bookingService.getMyBookings(passengerId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Your bookings", response));
    }

    /**
     * GET /api/bookings/ride-bookings
     * Get all bookings for rides created by current driver (DRIVER only)
     */
    @GetMapping("/ride-bookings")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getBookingsForMyRides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long driverId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);

        Page<BookingResponse> response = bookingService.getBookingsForMyRides(driverId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Bookings for your rides", response));
    }

    /**
     * PUT /api/bookings/{bookingId}/confirm
     * Confirm a booking (DRIVER only)
     * Reduces available seats when confirmed
     */
    @PutMapping("/{bookingId}/confirm")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(
            @PathVariable Long bookingId) {

        Long driverId = getCurrentUserId();
        BookingResponse response = bookingService.confirmBooking(bookingId, driverId);

        return ResponseEntity.ok(ApiResponse.success(
                "Booking confirmed and passenger added to ride",
                response
        ));
    }

    /**
     * DELETE /api/bookings/{bookingId}
     * Cancel a booking (PASSENGER or DRIVER)
     * Returns seat to available pool if was confirmed
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long bookingId) {

        Long userId = getCurrentUserId();
        BookingResponse response = bookingService.cancelBooking(bookingId, userId);

        return ResponseEntity.ok(ApiResponse.success(
                "Booking cancelled successfully",
                response
        ));
    }

    /**
     * Utility method to extract current user ID from authentication
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // In production, retrieve userId from UserDetails
        return Long.valueOf(1); // Placeholder - implement properly
    }
}
