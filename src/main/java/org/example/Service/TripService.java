package org.example.Service;

import org.example.DTO.DestinationDto;
import org.example.DTO.TripDto;
import org.example.entity.Destination;
import org.example.entity.Trip;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip createTrip(TripDto tripDto);
void updateTrip(int tripId, TripDto trip);
    List<Trip> getAllTrips();
    void deleteTripById(int tripId);
    Trip findTripById(int tripId);
    void removeTripForUser(int userId, int tripId);
    boolean ifTripExistsWithSuchId(int tripId);
    Integer getCityFromTripById(int tripId);
    Trip addDestinationToTrip(int tripId,int destinationId);
     List<Destination> getAllDestinationsByTripId(int tripId);
    List<Trip> getAllDestinationsByUserId(int userId);
   void deleteDestinationById(int tripId,int destinationId);
    List<Trip> getTripByUsersId(int userId);
    List<Trip> findTripsByUserIdAndCountryName( int userId,String countryName);
    List<Trip> SearchTripByPrefix(String loginPrefix);
}
