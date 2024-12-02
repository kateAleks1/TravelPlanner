package org.example.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.DTO.TripDto;
import org.example.DTO.UserDto;
import org.example.Dal.Repository.TripRepository;
import org.example.Service.ServiceImpl.TripServiceImpl;
import org.example.Service.TripService;
import org.example.Service.UserService;
import org.example.entity.Trip;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trip")
public class TripController {
@Autowired
    private final TripService
        tripService;
    @Autowired
private final TripRepository tripRepository;
    @Autowired
private final UserService userService;
    @Autowired
    public TripController(UserService userService, TripRepository tripRepository, TripService tripService) {
        this.userService = userService;
        this.tripRepository = tripRepository;
        this.tripService = tripService;
    }
@GetMapping("/getAllTripsByUserId/{userId}")
public ResponseEntity<?> createNewTrip(@PathVariable int userId) {
    List<Trip> trips = tripService.getTripByUsersId(userId);
    return ResponseEntity.ok(trips);
}
    @PostMapping("/createNewTrip")
    public ResponseEntity<?> createNewTrip(@RequestBody TripDto tripDto) {

       Trip trip= tripService.createTrip(tripDto);

        return ResponseEntity.ok(trip);

    }
    @GetMapping("/getAllTrips")
    public ResponseEntity<?> getAllTrips() {
            List<Trip> trips = tripService.getAllTrips();
            return ResponseEntity.ok(trips);
    }
    @DeleteMapping("/deleteTrip/{tripId}")
    public ResponseEntity<String> deleteTrip(@PathVariable int tripId) {
        tripService.deleteTripById(tripId);
        return ResponseEntity.ok("Trip with ID " + tripId + " has been deleted successfully.");
    }
    // getDestinationsFromUserId
    @GetMapping("/getDestinations/{tripId}")
    public ResponseEntity<?> getAllDestinationsFromTrip(@PathVariable int tripId){
        return ResponseEntity.ok(tripService.getAllDestinationsByTripId(tripId));
    }
    @PutMapping("/{tripId}/addDestination/{destinationId}")
    public ResponseEntity<?> addDestinationToTrip(@PathVariable int tripId, @PathVariable int destinationId){
return ResponseEntity.ok(tripService.addDestinationToTrip(tripId,destinationId));
    }
    @GetMapping("/searchTrips")
    public ResponseEntity<?> SearchTripByPrefix(@RequestParam String query){
        return ResponseEntity.ok(tripService.SearchTripByPrefix(query));
    }
    @DeleteMapping("/{tripId}/deleteDestinationById/{destinationId}")
    public ResponseEntity<?> deleteDestinationById(@PathVariable int tripId, @PathVariable int destinationId){
        tripService.deleteDestinationById(tripId,destinationId);
        return ResponseEntity.ok("build");
    }
    @GetMapping("/getTripById/{tripId}")
    public ResponseEntity<?> getTripById(@PathVariable int tripId){
        return ResponseEntity.ok(tripService.findTripById(tripId));
    }
    @GetMapping("/getCityFromTripById/{tripId}")
    public ResponseEntity<?> getCityFromTripById(@PathVariable int tripId){
        return ResponseEntity.ok(Map.of("cityId",tripService.getCityFromTripById(tripId)));
    }
    @GetMapping("/getAllTripByUserId/{userId}")
    public ResponseEntity<?> getAllTripByUserId(@PathVariable int userId){
        return ResponseEntity.ok(tripService.getAllDestinationsByUserId(userId));
    }
    @GetMapping("/findTripsByCountryName/{userId}/{countryName}")
    public ResponseEntity<?> findTripsByCountryName(@PathVariable int userId,@PathVariable String countryName){
        List<Trip> trips=tripService.findTripsByUserIdAndCountryName(userId,countryName);
        return ResponseEntity.ok(trips);
    }
//    @PutMapping("/updateTrips/{tripsId}")
//    public ResponseEntity<?> updateTrios(@PathVariable int tripsId, @RequestBody TripDto tripDto){
//        if(!userService.chechIfUserIdExists(tripDto.getUser_id()).isPresent()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "user with such id dont exist"));
//        }
// tripService.updateTrip(tripsId,tripDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Поездка успешно создана"));
//    }
//        @GetMapping("/getAllTripsFromAllUsersId")
//        public ResponseEntity<?> getAllTripsFromAllUsersId(){
//    List<Trip> trips=tripService.getAllTrips();
//            Set<Integer> tripUserIds = new HashSet<>();
//            trips.forEach(trip -> {
//                trip.getUser().forEach(user -> tripUserIds.add(user.getId())); // добавляем все ID пользователей
//            });
//
//            Optional<List<User>> usersList = userService.findListUserById(new ArrayList<>(tripUserIds));
//            if (usersList.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users not found for provided trip IDs");
//            }
//            return ResponseEntity.ok(usersList.get());
//        }
@GetMapping("/getAllTripsFromUserId/{userId}")
public ResponseEntity<?> getAllTripsFromUserId(@PathVariable String userId) {
    // Use repository to fetch trips directly
    Optional<List<Trip>> tripsOptional = tripRepository.getTripByUsersLogin(userId);

    // Check if trips exist for the user
    if (tripsOptional.isEmpty() || tripsOptional.get().isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No trips found for the provided user ID.");
    }

    // Return the list of trips
    return ResponseEntity.ok(tripsOptional.get());
}

    @DeleteMapping("/deleteTripForUser/{userId}/{tripId}")
    public ResponseEntity<?> deleteTripForUser(
            @PathVariable int userId,
            @PathVariable int tripId) {
        tripService.removeTripForUser(userId, tripId);
        return ResponseEntity.ok("Trip successfully removed");
    }
  //  @GetMapping("/getAllTripsFromSpecificUserId/{userId}")
    //public ResponseEntity<?> getAllTripsFromSpecificUserId(@PathVariable String userId){
//        List<Trip> trips = tripService.getAllTrips();
//
//        try {
//            int userIdInt = Integer.parseInt(userId); // Преобразуем userId в int
//            List<Trip> tripList = trips.stream()
//                    .filter(trip -> trip.getUser().stream()
//                            .anyMatch(user -> user.getId().equals(userIdInt))) // фильтруем поездки по ID пользователя
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(tripList);
//        } catch (NumberFormatException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
     //   }
    }


//    @RequestMapping("/error")
//
//    @GetMapping("/getAllTripsFromSpecificUserLogin/{userLogin}")
//    public ResponseEntity<?> getAllTripsFromSpecificUserLogin(@PathVariable String userLogin){
//        List<Trip> trips = tripService.getAllTrips();
//
//
//        List<Trip> tripList = trips.stream()
//                .filter(trip -> trip.getUser().stream()
//                        .anyMatch(user -> user.getId().equals(userLogin))) // проверяем, есть ли пользователь в поездке
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(tripList);
//    }

