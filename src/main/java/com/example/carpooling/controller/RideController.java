package com.carpooling.controller;

import com.carpooling.dto.request.RideCreateRequest;
import com.carpooling.dto.request.RideSearchRequest;
import com.carpooling.dto.response.ApiResponse;
import com.carpooling.dto.response.RideResponse;
import com.carpooling.service.RideService;
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
 * Ride Controller
 * Handles ride creation, search, and retrieval
 */
@RestController
@RequestMapping("/rides")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * POST /api/rides
     * Create a new ride (DRIVER only)
     */
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<RideResponse>> createRide(
            @Valid @RequestBody RideCreateRequest request) {

        // Get current driver ID from authentication
        Long driverId = getCurrentUserId();

        RideResponse response = rideService.createRide(request, driverId);
        return new ResponseEntity<>(
                ApiResponse.success("Ride created successfully", response),
                HttpStatus.CREATED
        );
    }

    /**
     * GET /api/rides/{rideId}
     * Get ride details
     */
    @GetMapping("/{rideId}")
    public ResponseEntity<ApiResponse<RideResponse>> getRide(
            @PathVariable Long rideId) {

        RideResponse response = rideService.getRideById(rideId);
        return ResponseEntity.ok(ApiResponse.success("Ride found", response));
    }

    /**
     * POST /api/rides/search
     * Search for available rides with pagination
     * Query params: page=0, size=10 (default)
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<RideResponse>>> searchRides(
            @Valid @RequestBody RideSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RideResponse> response = rideService.searchRides(request, pageable);

        return ResponseEntity.ok(ApiResponse.success(
                "Found " + response.getTotalElements() + " rides",
                response
        ));
    }

    /**
     * GET /api/rides/my-rides
     * Get all rides created by current driver (DRIVER only)
     */
    @GetMapping("/my-rides")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<Page<RideResponse>>> getMyRides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long driverId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);

        Page<RideResponse> response = rideService.getMyRides(driverId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Your rides", response));
    }

    /**
     * Utility method to extract current user ID from authentication
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // In production, you'd retrieve userId from UserDetails
        // For now, we parse from the Principal
        return Long.valueOf(1); // Placeholder - implement properly
    }
}
