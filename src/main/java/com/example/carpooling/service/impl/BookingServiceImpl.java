package com.carpooling.service.impl;

import com.carpooling.dto.response.BookingResponse;
import com.carpooling.entity.Booking;
import com.carpooling.entity.Ride;
import com.carpooling.entity.User;
import com.carpooling.entity.enums.BookingStatus;
import com.carpooling.exception.BookingException;
import com.carpooling.exception.ResourceNotFoundException;
import com.carpooling.exception.UnauthorizedException;
import com.carpooling.repository.BookingRepository;
import com.carpooling.repository.RideRepository;
import com.carpooling.repository.UserRepository;
import com.carpooling.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for booking management
 * Enforces critical rules like overbooking prevention and duplicate booking prevention
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              RideRepository rideRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    /**
     * Book a ride for a passenger
     *
     * Business rules enforced:
     * 1. Passenger cannot be the driver
     * 2. Passenger cannot book the same ride twice
     * 3. Ride must have available seats
     * 4. Booking created in REQUESTED status (driver must confirm)
     */
    @Override
    public BookingResponse bookRide(Long rideId, Long passengerId) {

        // Fetch ride
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        // Fetch passenger
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with ID: " + passengerId));

        // Rule 1: Passenger cannot be the driver
        if (ride.getDriver().getId().equals(passengerId)) {
            throw new BookingException("Driver cannot book their own ride");
        }

        // Rule 2: Check for duplicate booking
        if (bookingRepository.findByRideIdAndPassengerId(rideId, passengerId).isPresent()) {
            throw new BookingException("Passenger has already booked this ride");
        }

        // Rule 3: Check seat availability
        if (ride.getAvailableSeats() <= 0) {
            throw new BookingException("No available seats for this ride");
        }

        // Create booking (REQUESTED status = pending driver confirmation)
        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setStatus(BookingStatus.REQUESTED);

        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }

    /**
     * Retrieve booking by ID
     */
    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        return new BookingResponse(booking);
    }

    /**
     * Retrieve all bookings made by a passenger
     */
    @Override
    public Page<BookingResponse> getMyBookings(Long passengerId, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findByPassengerId(passengerId, pageable);
        return bookings.map(BookingResponse::new);
    }

    /**
     * Retrieve all bookings for rides created by a driver
     */
    @Override
    public Page<BookingResponse> getBookingsForMyRides(Long driverId, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findBookingsForDriverRides(driverId, pageable);
        return bookings.map(BookingResponse::new);
    }

    /**
     * Confirm a booking
     * Only the ride's driver can confirm a booking
     * When confirmed:
     * - Booking status changes to CONFIRMED
     * - Available seats reduced by 1
     */
    @Override
    public BookingResponse confirmBooking(Long bookingId, Long driverId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        // Authorization check: only driver can confirm
        if (!booking.getRide().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the ride driver can confirm a booking");
        }

        // Only REQUESTED bookings can be confirmed
        if (!booking.getStatus().equals(BookingStatus.REQUESTED)) {
            throw new BookingException("Only REQUESTED bookings can be confirmed");
        }

        // Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);

        // Reduce available seats
        Ride ride = booking.getRide();
        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }

    /**
     * Cancel a booking
     * Passenger can cancel their own booking
     * Driver can cancel any booking for their ride
     * When cancelled:
     * - Booking status changes to CANCELLED
     * - If was CONFIRMED, available seats increased by 1
     */
    @Override
    public BookingResponse cancelBooking(Long bookingId, Long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        // Authorization check
        boolean isPassenger = booking.getPassenger().getId().equals(userId);
        boolean isDriver = booking.getRide().getDriver().getId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new UnauthorizedException("You don't have permission to cancel this booking");
        }

        // Can't cancel already cancelled bookings
        if (booking.getStatus().equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking is already cancelled");
        }

        // If booking was confirmed, return seat to available pool
        if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            Ride ride = booking.getRide();
            ride.setAvailableSeats(ride.getAvailableSeats() + 1);
            rideRepository.save(ride);
        }

        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(savedBooking);
    }
}
