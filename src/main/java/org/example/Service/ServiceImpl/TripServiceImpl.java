package org.example.Service.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.example.DTO.TripDto;
import org.example.Dal.Repository.TripRepository;
import org.example.Dal.Repository.UserRepository;
import org.example.Service.TripService;
import org.example.entity.Trip;
import org.example.entity.Trip_Status;
import org.example.entity.User;
import org.example.mapper.TripMapper;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service

public class TripServiceImpl implements TripService {


    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private  UserMapper userMapper;
    private  TripMapper tripMapper;
@Autowired
    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }


    @Override
    public boolean updateTrip(Trip trip) {
        return false; // Логика обновления будет добавлена позже
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public void deleteTrip(int tripId) {
        // Логика удаления будет добавлена позже
    }
    @Override
    public void createTrip(TripDto tripDto) {
        Trip trip = new Trip();
        trip.setStartDate(Timestamp.valueOf(tripDto.getStart_date()));
        trip.setEndDate(Timestamp.valueOf(tripDto.getEnd_date()));
        trip.setUser(userRepository.findUserById(tripDto.getUser_id()).get());
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
