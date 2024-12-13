package org.example.Service;

import org.example.DTO.CityStatistic;
import org.example.DTO.DestinationsStatistic;
import org.example.DTO.TripDto;
import org.example.entity.Destination;
import org.example.entity.Trip;

import java.util.Date;
import java.util.List;

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
    List<Trip> filterTripsByDate(Date startDate, Date endDate);
     void updateTripStatuses();
    void updateupreatedAtDates();
    List<Object[]> getMostPopularTrips();
    List<DestinationsStatistic>  findMostCommonDestination();
    List<DestinationsStatistic> findAllMostCommonDestination();
    List<Trip> getAloneTrip(int userId);
    List<Trip> getTripByGroup(int userId);
}
