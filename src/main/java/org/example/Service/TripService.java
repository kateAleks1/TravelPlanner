package org.example.Service;

import org.example.DTO.TripDto;
import org.example.entity.Trip;

import java.util.List;
import java.util.Optional;

public interface TripService {
   void createTrip(TripDto tripDto);
void updateTrip(int tripId, TripDto trip);
    List<Trip> getAllTrips();
    void deleteTrip(int tripId);
    Optional<Trip> findTripById(int tripId);
    boolean ifTripExistsWithSuchId(int tripId);
}
