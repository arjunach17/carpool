package com.carpooling.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rides", indexes = {
        @Index(name = "idx_source_dest_date", columnList = "source, destination, ride_date"),
        @Index(name = "idx_driver_id", columnList = "driver_id")
})
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many rides belong to one driver
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @NotBlank(message = "Source is required")
    @Column(nullable = false, length = 100)
    private String source;

    @NotBlank(message = "Destination is required")
    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false)
    private LocalDate rideDate;

    @Column(nullable = false)
    private LocalTime rideTime;

    @Positive(message = "Total seats must be greater than 0")
    @Column(nullable = false)
    private Integer totalSeats;

    @Positive(message = "Available seats must be greater than 0")
    @Column(nullable = false)
    private Integer availableSeats;

    @Positive(message = "Price per seat must be greater than 0")
    @Column(nullable = false)
    private Double pricePerSeat;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One ride can have many bookings
    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Set<Booking> getBookings() { return bookings; }
    public void setBookings(Set<Booking> bookings) { this.bookings = bookings; }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", rideDate=" + rideDate +
                ", rideTime=" + rideTime +
                ", availableSeats=" + availableSeats +
                ", pricePerSeat=" + pricePerSeat +
                '}';
    }
}
