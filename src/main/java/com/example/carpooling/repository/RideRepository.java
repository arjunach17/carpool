package com.carpooling.repository;

import com.carpooling.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    /**
     * Find available rides by source, destination, and date with pagination
     * Available rides = those with available_seats > 0
     */
    @Query("SELECT r FROM Ride r WHERE " +
            "LOWER(r.source) = LOWER(:source) AND " +
            "LOWER(r.destination) = LOWER(:destination) AND " +
            "r.rideDate = :rideDate AND " +
            "r.availableSeats > 0")
    Page<Ride> findAvailableRides(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("rideDate") LocalDate rideDate,
            Pageable pageable
    );

    /**
     * Find all rides created by a specific driver
     */
    List<Ride> findByDriverId(Long driverId);

    /**
     * Find rides by driver with pagination
     */
    Page<Ride> findByDriverId(Long driverId, Pageable pageable);

/**
 * Find rides fo
