package com.carpooling.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @NotNull(message = "Ride ID is required")
    @Positive(message = "Ride ID must be a positive number")
    private Long rideId;

    // Getters and Setters
    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }
}
