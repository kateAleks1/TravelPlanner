package org.example.Service;

import org.example.DTO.TripDto;

import java.util.Optional;

public interface TripService {
    Optional<TripDto> createTrip(TripDto tripDto);
boolean updateTrip(TripDto tripDto);
void deleteTrip(int TripId);
Optional<TripDto> findTripbyId(int TripId);
}
