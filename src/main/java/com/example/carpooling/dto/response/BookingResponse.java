package com.carpooling.dto.response;

import com.carpooling.entity.Booking;

public class BookingResponse {

    private Long id;
    private Long rideId;
    private Long passengerId;
    private String passengerUsername;
    private String status;
    private String bookedAt;

    public BookingResponse() {}

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.rideId = booking.getRide().getId();
        this.passengerId = booking.getPassenger().getId();
        this.passengerUsername = booking.getPassenger().getUsername();
        this.status = booking.getStatus().toString();
        this.bookedAt = booking.getBookedAt().toString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public String getPassengerUsername() { return passengerUsername; }
    public void setPassengerUsername(String passengerUsername) { this.passengerUsername = passengerUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookedAt() { return bookedAt; }
    public void setBookedAt(String bookedAt) { this.bookedAt = bookedAt; }
}
