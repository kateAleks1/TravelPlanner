package org.example.Service.ServiceImpl;

import org.example.DTO.TripDto;
import org.example.Dal.Repository.TripRepository;
import org.example.Dal.Repository.TripStatusRepository;
import org.example.Dal.Repository.UserRepository;
import org.example.Service.TripService;
import org.example.entity.Destination;
import org.example.entity.Trip;
import org.example.entity.Trip_Status;
import org.example.entity.User;

import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class TripServiceImpl implements TripService {


    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripStatusRepository tripStatusRepository;
    private  UserMapper userMapper;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository, TripStatusRepository tripStatusRepository) {
        this.tripRepository = tripRepository;
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
    public void createTrip(TripDto tripDto) {
        Trip trip = new Trip();
        trip.setStartDate(tripDto.getStart_date());
        trip.setEndDate(tripDto.getEnd_date());


        Trip_Status tripStatus = tripStatusRepository.findById(tripDto.getStatus_id())
                .orElseThrow(() -> new RuntimeException("Status ID not found"));
        trip.setStatusTrip(tripStatus);

        // Find the user and add to trip participants
        User user = userRepository.findById(tripDto.getUsers())
                .orElseThrow(() -> new RuntimeException("User with ID " + tripDto.getUsers() + " not found"));
        trip.getUsers().add(user);

        // Set other fields, like destination and price
        trip.setDestinations(List.of(new Destination().builder().destinationId(tripDto.getIdDestination()).build()));
        trip.setPrice(tripDto.getPrice());

        tripRepository.save(trip);
    }
    @Override
    public Optional<Trip> findTripById(int tripId) {
        return tripRepository.findById(tripId);
    }

    @Override
    public boolean ifTripExistsWithSuchId(int tripId) {
        return tripRepository.findById(tripId).isPresent();
    }
}
