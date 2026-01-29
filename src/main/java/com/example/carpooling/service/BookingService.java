package com.carpooling.service;

import com.carpooling.dto.response.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse bookRide(Long rideId, Long passengerId);
    BookingResponse getBookingById(Long bookingId);
    Page<BookingResponse> getMyBookings(Long passengerId, Pageable pageable);
    Page<BookingResponse> getBookingsForMyRides(Long driverId, Pageable pageable);
    BookingResponse confirmBooking(Long bookingId, Long driverId);
    BookingResponse cancelBooking(Long bookingId, Long userId);
}
