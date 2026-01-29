package com.carpooling.service;

import com.carpooling.dto.request.RideCreateRequest;
import com.carpooling.dto.request.RideSearchRequest;
import com.carpooling.dto.response.RideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RideService {
    RideResponse createRide(RideCreateRequest request, Long driverId);
    RideResponse getRideById(Long rideId);
    Page<RideResponse> searchRides(RideSearchRequest request, Pageable pageable);
    Page<RideResponse> getMyRides(Long driverId, Pageable pageable);
}
