package org.example.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;

import org.example.DTO.CityStatistic;
import org.example.DTO.DestinationsStatistic;
import org.example.DTO.TripDto;
import org.example.DTO.TripSortingCountries;
import org.example.Dal.Repository.*;
import org.example.Service.TripService;
import org.example.entity.*;

import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TaskScheduler taskScheduler;
    @Override
    public Integer getCityFromTripById(int tripId) {
        Integer trip=tripRepository.getCityFromTripById(tripId).get();
     return   trip;
    }

    @Override
    public List<Trip> filterTripsByDate(Date startDate, Date endDate, int userId) {
        return tripRepository.getTripsByUserIdAndDateRange(startDate,endDate,userId).get();
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
    public List<TripSortingCountries> findAllMostCommonTripsByCountry() {
        return tripRepository.findAllMostCommonTripsByCountry();
    }

    @Override
    public List<TripSortingCountries> findAllLeastCommonTripsByCountry() {
        return tripRepository.findAllLeastCommonTripsByCountry();
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


    public List<Trip> filterTripsByDate(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        return tripRepository.findTripsByDateRange(startDate, endDate);
    }

    @Override
    public List<DestinationsStatistic> findMostCommonDestination() {
        Pageable pageable= PageRequest.of(0,3);
        Page<Object[]> results=tripRepository.findMostCommonDestination(pageable);
        return results.stream()
                .map(result -> new DestinationsStatistic(
                        (String) result[1],
                        (Long) result[0])
                ).collect(Collectors.toList());

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
    public List<DestinationsStatistic> findAllMostCommonDestination() {
        return tripRepository.findAllMostCommonDestination();
    }

    @Scheduled(cron="0 0 0 * * ?")
    @Override
    public void updateupreatedAtDates() {
        Random random=new Random();
        String pattern="yyyy-MM-dd HH:mm:ss";
        List<Trip> allTrips=tripRepository.findAll();
        for(Trip trip:allTrips){
            if(trip.getCreatedAt()==null){

                trip.setCreatedAt(generateDate(pattern));
                while (trip.getCreatedAt().after(Date.from(Instant.now()))){
                    trip.setCreatedAt(generateDate(pattern));
                }
            }

        }
        tripRepository.saveAll(allTrips);
    }

    @Override
    public List<Object[]> getMostPopularTrips() {
        return tripRepository.findMostFrequentTrips();
    }

    public static Date generateDate(String pattern) {
        int year = 2024;
        int day = (int) ((Math.random() * 31) + 1);
        int month = (int) ((Math.random() * 12) + 1);
        int hours = (int) ((Math.random() * 24));
        int minutes = (int) ((Math.random() * 60));
        int seconds = (int) ((Math.random() * 60));

        String dates = pattern
                .replaceFirst("yyyy", String.format("%04d", year))
                .replaceFirst("MM", String.format("%02d", month))
                .replaceFirst("dd", String.format("%02d", day))
                .replaceFirst("HH", String.format("%02d", hours))
                .replaceFirst("mm", String.format("%02d", minutes))
                .replaceFirst("ss", String.format("%02d", seconds));
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime=LocalDateTime.parse(dates,formatter);
  return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    @Override
    public Trip createTrip(TripDto tripDto) {
      for(Trip tripEach:tripRepository.getTripByUsersId(tripDto.getUserOrganizerId()).get()){
          if(tripEach.getCity().getCityId()==tripDto.getCityId()){
              throw new RuntimeException("This city has already exist");
          }
      }
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

            Date startDate = dateOnlyFormat.parse(dateOnlyFormat.format(trip.getStartDate()));
            Date endDate = dateOnlyFormat.parse(dateOnlyFormat.format(trip.getEndDate()));
            Date today = dateOnlyFormat.parse(dateOnlyFormat.format(new Date()));

            if (startDate.after(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(3)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // IN_PROGRESS
            } else if (startDate.before(today) && endDate.after(today)) {
                trip.setStatusTrip(tripStatusRepository.findById(2)
                        .orElseThrow(() -> new RuntimeException("Status not found"))); // PLANNED
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


trip.setCreatedAt(tripDto.getCreatedAt());
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
    public List<Trip> SearchTripByPrefix(int userId,String loginPrefix){
        return tripRepository.findTripsByPrefix(userId,loginPrefix).get();
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
    public List<Destination> findDestinationsByPrefix(int tripId, String prefix) {
        return tripRepository.findDestinationsByPrefix(tripId,prefix).get();
    }

    @Override
    public List<Destination> findAllLikedDestinations(int userId) {
        return tripRepository.findAllLikedDestinations(userId).get();
    }

    @Override
    public String findAllTripBackgroundImagesByCityId(int userId, int tripId) {
        return tripRepository.findAllTripBackgroundImagesByCityId(userId,tripId).get();
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

    @Override
    public List<Trip> getAloneTrip(int userId) {
        return tripRepository.getAloneTrip(userId).get();
    }

    @Override
    public List<Trip> getTripByGroup(int userId) {
        return tripRepository.getTripByGroup(userId).get();
    }
}
