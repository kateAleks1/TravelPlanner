package org.example.Service.ServiceImpl;

import org.example.DTO.DestinationDto;
import org.example.DTO.TripDto;
import org.example.Dal.Repository.*;
import org.example.Service.TripService;
import org.example.entity.*;

import org.example.exception.GeneralException;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TripServiceImpl implements TripService {

    @Override
    public Integer getCityFromTripById(int tripId) {
        Trip trip=tripRepository.findById(tripId).get();
     return   trip.getCity().getCityId();
    }

    private final TripRepository tripRepository;
    private final CitiesRepository citiesRepository;
    private final DestinationsRepository destinationsRepository;
    private final UserRepository userRepository;
    private final TripStatusRepository tripStatusRepository;
    private  UserMapper userMapper;
@Autowired
    public TripServiceImpl(TripRepository tripRepository, CitiesRepository citiesRepository, DestinationsRepository destinationsRepository, UserRepository userRepository, TripStatusRepository tripStatusRepository) {
        this.tripRepository = tripRepository;
        this.citiesRepository = citiesRepository;
        this.destinationsRepository = destinationsRepository;
        this.userRepository = userRepository;
        this.tripStatusRepository = tripStatusRepository;
    }

    @Override
    public void updateTrip(int tripId, TripDto tripDto) {

        Optional<Trip> existingTrip = tripRepository.findById(tripId);

            Trip trip = existingTrip.get();

        userRepository.findById(tripDto.getUsers()).ifPresent(user -> {
            trip.getUsers().add(user);
        });

            tripRepository.save(trip);

    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public void deleteTrip(int tripId) {
       if(tripRepository.findById(tripId).isPresent()){
           tripRepository.delete(tripRepository.findById(tripId).get());
       }
    }
    @Override
    public Trip createTrip(TripDto tripDto) {
        Trip trip = new Trip();
        if(tripDto.getStart_date().before(tripDto.getEnd_date()) && !tripDto.getStart_date().before(Date.from(Instant.now())) && !tripDto.getEnd_date().before(Date.from(Instant.now()) )){
            trip.setStartDate(tripDto.getStart_date());
            trip.setEndDate(tripDto.getEnd_date());
        }
        Optional<Trip_Status> tripStatus = tripStatusRepository.findById(1);
        trip.setStatusTrip(tripStatus.get());
        User user = userRepository.findById(tripDto.getUsers())
                .orElseThrow(() -> new RuntimeException("User with ID " + tripDto.getUsers() + " not found"));
        trip.getUsers().add(user);
        trip.setDestinations(List.of(new Destination().builder().destinationId(tripDto.getIdDestination()).build()));
trip.setCity(citiesRepository.findById(tripDto.getCityId()).get());
        trip.setPrice(tripDto.getPrice());
      return   tripRepository.save(trip);
    }

    @Override
    public Trip addDestinationToTrip(int tripId,int destinationId) {
        //поиск tripId по городу
       Trip trip=tripRepository.findById(tripId).get();
       Destination destination=destinationsRepository.findByDestinationId(destinationId).get();
       if(trip!=null && destination!=null && trip.getCity().equals(destination.getCities())){
           trip.getDestinations().add(destination);
           int cityId=destination.getCities().getCityId();
           Cities cities=citiesRepository.getById(cityId);
           trip.setCity(cities);
       }
      return tripRepository.save(trip);

    }

//    @Override
//    public List<Destination> deleteDestinationById(int tripId) {
//        Trip trip=tripRepository.findById(tripId).get();
//
//    }

    @Override
    public List<Destination> getAllDestinationsByTripId(int tripId) {

        return  tripRepository.findDestinationsByTripId(tripId).get();
    }

    @Override
    public Trip findTripById(int tripId) {
        return tripRepository.findById(tripId).get();
    }

    @Override
    public boolean ifTripExistsWithSuchId(int tripId) {
        return tripRepository.findById(tripId).isPresent();
    }
}
