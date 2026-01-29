package com.carpooling.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RideSearchRequest {

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Ride date is required")
    private LocalDate rideDate;

    // Getters and Setters
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getRideDate() { return rideDate; }
    public void setRideDate(LocalDate rideDate) { this.rideDate = rideDate; }
}
