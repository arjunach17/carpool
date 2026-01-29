package com.carpooling.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class RideCreateRequest {

    @NotBlank(message = "Source is required")
    @Size(min = 2, max = 100, message = "Source must be between 2 and 100 characters")
    private String source;

    @NotBlank(message = "Destination is required")
    @Size(min = 2, max = 100, message = "Destination must be between 2 and 100 characters")
    private String destination;

    @NotNull(message = "Ride date is required")
    @FutureOrPresent(message = "Ride date must be today or in the future")
    private LocalDate rideDate;

    @NotNull(message = "Ride time is required")
    private LocalTime rideTime;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be greater than 0")
    private Integer totalSeats;

    @NotNull(message = "Price per seat is required")
    @Positive(message = "Price per seat must be greater than 0")
    private Double pricePerSeat;

    // Getters and Setters
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getRideDate() { return rideDate; }
    public void setRideDate(LocalDate rideDate) { this.rideDate = rideDate; }

    public LocalTime getRideTime() { return rideTime; }
    public void setRideTime(LocalTime rideTime) { this.rideTime = rideTime; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Double getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(Double pricePerSeat) { this.pricePerSeat = pricePerSeat; }
}
