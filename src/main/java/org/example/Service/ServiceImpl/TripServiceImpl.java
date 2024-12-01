package org.example.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.example.DTO.DestinationDto;
import org.example.DTO.TripDto;
import org.example.Dal.Repository.*;
import org.example.Service.TripService;
import org.example.entity.*;

import org.example.exception.GeneralException;
import org.example.mapper.UserMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TripServiceImpl implements TripService {

    @Override
    public Integer getCityFromTripById(int tripId) {
        Integer trip=tripRepository.getCityFromTripById(tripId).get();
     return   trip;
    }

    private final TripRepository tripRepository;
    private final CitiesRepository citiesRepository;
    private final DestinationsRepository destinationsRepository;
    private final UserRepository userRepository;
    private final TripStatusRepository tripStatusRepository;
    private final TripParticipantRepository tripParticipantRepository;

    private  UserMapper userMapper;
@Autowired
    public TripServiceImpl( TripRepository tripRepository, CitiesRepository citiesRepository, DestinationsRepository destinationsRepository, UserRepository userRepository, TripStatusRepository tripStatusRepository, TripParticipantRepository tripParticipantRepository) {

        this.tripRepository = tripRepository;
        this.citiesRepository = citiesRepository;
        this.destinationsRepository = destinationsRepository;
        this.userRepository = userRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.tripParticipantRepository = tripParticipantRepository;
    }

    @Override
    public void updateTrip(int tripId, TripDto tripDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with ID: " + tripId));

        // Обновление участников
        for(Integer userId:tripDto.getUserIds()){
            userRepository.findById(userId).ifPresent(user -> {
                // Проверяем, есть ли уже связь между пользователем и поездкой
                boolean exists = tripParticipantRepository.findByUserAndTrip(user, trip).isPresent();

                if (!exists) {
                    TripPartcipants participants = new TripPartcipants();
                    participants.setId(new TripParticipantId(trip.getTripId(), user.getId()));
                    participants.setTrip(trip);
                    participants.setUser(user);
                    participants.setOrganizer(tripDto.isOrganizer());
                    participants.setGroup(tripDto.isGroup());

                    tripParticipantRepository.save(participants);
                }
            });
        }


        // Сохранение изменений в поездке
        tripRepository.save(trip);
    }


    @Override
    public List<Trip> getAllTrips() {
    //update trip status through date
        List<Trip> trips = tripRepository.findAll();
        trips.forEach(trip -> {
            System.out.println("Trip: " + trip.getTripId() + ", Participants: " + trip.getParticipants());
        });
        return trips;
    }
    public void removeTripForUser(int userId, int tripId) {
        // Используем JPA или Native Query для удаления записи
        tripRepository.deleteByUserIdAndTripId(userId, tripId);
    }
    @Transactional
    public void deleteTripById(int tripId) {
        // Удаляем участников поездки
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip with ID " + tripId + " not found"));

        // Удалить участников
        tripParticipantRepository.deleteAllByTrip(trip);

        // Удалить поездку
        tripRepository.delete(trip);
    }

    @Override
    public List<Trip> getTripByUsersId(int userId) {
        return tripRepository.getTripByUsersId(userId).get();
    }
    @Transactional
    @Override
    public void deleteDestinationById(int tripId,int destinationId) {
         tripRepository.deleteDestinationFromTrip(tripId,destinationId);
    }

    @Override
    public Trip createTrip(TripDto tripDto) {
        Trip trip = new Trip();

        // Установка дат
        if (tripDto.getStart_date().before(tripDto.getEnd_date()) &&
                !tripDto.getEnd_date().before(Date.from(Instant.now()))) {
            trip.setStartDate(tripDto.getStart_date());
            trip.setEndDate(tripDto.getEnd_date());
        } else {
            throw new RuntimeException("Invalid start_date or end_date");
        }

        // Установка статуса поездки
        if (trip.getStartDate().after(Date.from(Instant.now()))) {
            trip.setStatusTrip(tripStatusRepository.findById(4)
                    .orElseThrow(() -> new RuntimeException("Status not found")));
        } else {
            trip.setStatusTrip(tripStatusRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Status not found")));
        }

        // Установка города
        trip.setCity(citiesRepository.findById(tripDto.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found")));

        trip = tripRepository.save(trip);

        if (tripDto.getUsersListId() != null && !tripDto.getUsersListId().isEmpty()) {
            for (Integer userId : tripDto.getUsersListId()) {

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                TripPartcipants participants = new TripPartcipants();
                participants.setId(new TripParticipantId(trip.getTripId(), user.getId()));
                participants.setTrip(trip);
                participants.setUser(user);
                if(user.getId()==tripDto.getUserOrganizerId()){
                    participants.setOrganizer(true);
                }else{
                    participants.setOrganizer(false);
                }



                if(tripDto.getUsersListId().size()>1){
                    participants.setGroup(true);
                }else{
                    participants.setGroup(false);
                }

                tripParticipantRepository.save(participants);
            }
        } else {
            throw new RuntimeException("No users provided for the trip");
        }

        return trip;
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
    public List<Trip> SearchTripByPrefix(String loginPrefix){
        return tripRepository.findTripsByPrefix(loginPrefix).get();
    }

    @Override
    public List<Trip> findTripsByUserIdAndCountryName(int userId,String countryName) {
        return tripRepository.findTripsByUserIdAndCountryName(userId,countryName);
    }
    @Override
    public List<Destination> getAllDestinationsByTripId(int tripId) {

        return  tripRepository.findDestinationsByTripId(tripId).get();
    }

    @Override
    public List<Trip> getAllDestinationsByUserId(int userId) {
        return tripRepository.getTripByUsersId(userId).get();
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
