package org.example.Service.ServiceImpl;

import jakarta.annotation.PostConstruct;
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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TaskScheduler taskScheduler;
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
        if (tripDto.getStart_date() != null && tripDto.getStart_date().toString()!="") {
            trip.setStartDate(tripDto.getStart_date());
        }

        if (tripDto.getEnd_date() != null && tripDto.getEnd_date().toString()!="") {
            trip.setEndDate(tripDto.getEnd_date());
        }

        for(Integer userId:tripDto.getUsersListId()){
            userRepository.findById(userId).ifPresent(user -> {

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

        tripRepository.save(trip);
    }


    @Override
    public List<Trip> getAllTrips() {

        List<Trip> trips = tripRepository.findAll();
        trips.forEach(trip -> {
            System.out.println("Trip: " + trip.getTripId() + ", Participants: " + trip.getParticipants());
        });
        return trips;
    }
    public void removeTripForUser(int userId, int tripId) {

        tripRepository.deleteByUserIdAndTripId(userId, tripId);
    }
    @Transactional
    public void deleteTripById(int tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip with ID " + tripId + " not found"));

        tripParticipantRepository.deleteAllByTrip(trip);

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
    public List<Trip> filterTripsByDate(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        return tripRepository.findTripsByDateRange(startDate, endDate);
    }

//    public void forceUpdateTripStatuses() {
//        taskScheduler.schedule(this::updateTripStatuses, new Date());  // Немедленное выполнение
//    }
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
public void updateTripStatuses(){
LocalDate today=LocalDate.now();

List<Trip>trips=tripRepository.findAll();
for(Trip trip:trips){
LocalDate tripStartDate=trip.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
LocalDate tripEndDate=trip.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
if(tripStartDate.isBefore(today)){
    trip.setStatusTrip(tripStatusRepository.findById(2)
            .orElseThrow(() -> new RuntimeException("Status not found")));
}else if(tripStartDate.equals(today)){
    trip.setStatusTrip(tripStatusRepository.findById(1)
            .orElseThrow(() -> new RuntimeException("Status not found")));
}
    else if(today.isAfter(tripStartDate) && today.isBefore(tripEndDate)){
    trip.setStatusTrip(tripStatusRepository.findById(3)
            .orElseThrow(() -> new RuntimeException("Status not found")));
}
else{
    trip.setStatusTrip(tripStatusRepository.findById(4)
            .orElseThrow(() -> new RuntimeException("Status not found")));
}
}
tripRepository.saveAll(trips);

}
    @Override
    public Trip createTrip(TripDto tripDto) {
        Trip trip = new Trip();


        if (tripDto.getStart_date().before(tripDto.getEnd_date()) &&
                !tripDto.getEnd_date().before(Date.from(Instant.now()))) {
            trip.setStartDate(tripDto.getStart_date());
            trip.setEndDate(tripDto.getEnd_date());
        } else {
            throw new RuntimeException("Invalid start_date or end_date");
        }
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Получаем только дату без времени
            Date startDate = dateOnlyFormat.parse(dateOnlyFormat.format(trip.getStartDate()));
            Date endDate = dateOnlyFormat.parse(dateOnlyFormat.format(trip.getEndDate()));
            Date today = dateOnlyFormat.parse(dateOnlyFormat.format(new Date()));

            if (startDate.after(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(2)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // PLANNED
            } else if (startDate.before(today) && endDate.after(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(3)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // IN_PROGRESS
            } else if (endDate.before(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(4)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // FINISHED
            } else if (startDate.equals(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(1)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // STARTED
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
