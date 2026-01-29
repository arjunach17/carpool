package com.carpooling.service.impl;

import com.carpooling.dto.request.RideCreateRequest;
import com.carpooling.dto.request.RideSearchRequest;
import com.carpooling.dto.response.RideResponse;
import com.carpooling.entity.Ride;
import com.carpooling.entity.User;
import com.carpooling.exception.ResourceNotFoundException;
import com.carpooling.repository.RideRepository;
import com.carpooling.repository.UserRepository;
import com.carpooling.service.RideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for ride management
 */
@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideServiceImpl(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new ride
     * Driver specifies route, date, time, seats, and price
     */
    @Override
    public RideResponse createRide(RideCreateRequest request, Long driverId) {

        // Fetch driver
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        // Create ride entity
        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setSource(request.getSource());
        ride.setDestination(request.getDestination());
        ride.setRideDate(request.getRideDate());
        ride.setRideTime(request.getRideTime());
        ride.setTotalSeats(request.getTotalSeats());
        ride.setAvailableSeats(request.getTotalSeats()); // Initially all seats available
        ride.setPricePerSeat(request.getPricePerSeat());

        Ride savedRide = rideRepository.save(ride);
        return new RideResponse(savedRide);
    }

    /**
     * Retrieve ride by ID
     */
    @Override
    public RideResponse getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));
        return new RideResponse(ride);
    }

    /**
     * Search rides by source, destination, and date
     * Returns only rides with available seats (availableSeats > 0)
     * Supports pagination
     */
    @Override
    public Page<RideResponse> searchRides(RideSearchRequest request, Pageable pageable) {
        Page<Ride> rides = rideRepository.findAvailableRides(
                request.getSource(),
                request.getDestination(),
                request.getRideDate(),
                pageable
        );
        return rides.map(RideResponse::new);
    }

    /**
     * Retrieve all rides created by a specific driver
     */
    @Override
    public Page<RideResponse> getMyRides(Long driverId, Pageable pageable) {
        Page<Ride> rides = rideRepository.findByDriverId(driverId, pageable);
        return rides.map(RideResponse::new);
    }
}
