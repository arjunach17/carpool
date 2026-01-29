package com.carpooling.dto.response;

import com.carpooling.entity.Ride;
import java.time.LocalDate;
import java.time.LocalTime;

public class RideResponse {

    private Long id;
    private Long driverId;
    private String driverUsername;
    private String source;
    private String destination;
    private LocalDate rideDate;
    private LocalTime rideTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double pricePerSeat;
    private Integer bookedSeats;

    public RideResponse() {}

    public RideResponse(Ride ride) {
        this.id = ride.getId();
        this.driverId = ride.getDriver().getId();
        this.driverUsername = ride.getDriver().getUsername();
        this.source = ride.getSource();
        this.destination = ride.getDestination();
        this.rideDate = ride.getRideDate();
        this.rideTime = ride.getRideTime();
        this.totalSeats = ride.getTotalSeats();
        this.availableSeats = ride.getAvailableSeats();
        this.pricePerSeat = ride.getPricePerSeat();
        this.bookedSeats = ride.getTotalSeats() - ride.getAvailableSeats();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverUsername() { return driverUsername; }
    public void setDriverUsername(String driverUsername) { this.driverUsername = driverUsername; }

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

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Double getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(Double pricePerSeat) { this.pricePerSeat = pricePerSeat; }

    public Integer getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(Integer bookedSeats) { this.bookedSeats = bookedSeats; }
}
