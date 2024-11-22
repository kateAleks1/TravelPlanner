package org.example.Service;

import org.example.entity.Trip;
import org.example.entity.Trip_Status;

import java.util.List;
import java.util.Optional;

public interface StatusTripService {
    Trip_Status findStatusTripById(int tripId);
    List<Trip_Status> getAllTrips();
}
