package com.carpooling.repository;

import com.carpooling.entity.Booking;
import com.carpooling.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find existing booking to prevent duplicate
     * Returns Optional - present if passenger already booked this ride
     */
    Optional<Booking> findByRideIdAndPassengerId(Long rideId, Long passengerId);

    /**
     * Find all bookings for a specific ride
     */
    List<Booking> findByRideId(Long rideId);

    /**
     * Find all confirmed bookings for a ride
     */
    List<Booking> findByRideIdAndStatus(Long rideId, BookingStatus status);

    /**
     * Find all bookings made by a specific passenger
     */
    Page<Booking> findByPassengerId(Long passengerId, Pageable pageable);

    /**
     * Find all bookings for rides created by a specific driver
     */
    @Query("SELECT b FROM Booking b WHERE b.ride.driver.id = :driverId")
    Page<Booking> findBookingsForDriverRides(@Param("driverId") Long driverId, Pageable pageable);

    /**
     * Count confirmed bookings for a ride
     */
    long countByRideIdAndStatus(Long rideId, BookingStatus status);

    /**
     * Find all bookings with specific status
     */
    List<Booking> findByStatus(BookingStatus status);
}
