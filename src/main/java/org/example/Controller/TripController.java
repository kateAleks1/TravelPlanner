package org.example.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.DTO.TripDto;
import org.example.DTO.UserDto;
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
    private final TripService tripService;
@Autowired
private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping("/createNewTrip")
    public ResponseEntity<?> createNewTrip(@RequestBody TripDto tripDto) {
        if(tripDto.getStart_date().after(tripDto.getEnd_date()) || tripDto.getStart_date().before(Date.from(Instant.now())) || tripDto.getEnd_date().before(Date.from(Instant.now()) )){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","wrong dates"));
        }
       Trip trip= tripService.createTrip(tripDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("tripId", trip.getTripId()));

    }
    @GetMapping("/getAllTrips")
    public ResponseEntity<?> getAllTrips() {
            List<Trip> trips = tripService.getAllTrips();
            return ResponseEntity.ok(trips);
    }
    @DeleteMapping("deleteTrip/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable int tripId){
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
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
    @DeleteMapping("/{tripId}/deleteDestinationById/{destinationId}")
    public ResponseEntity<?> deleteDestinationById(@PathVariable int tripId, @PathVariable int destinationId){
        return ResponseEntity.ok(tripService.addDestinationToTrip(tripId,destinationId));
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
//@GetMapping("/getAllTripsFromUserId/{userId}")
//    public ResponseEntity<?> getAllTripsFromUserId(@PathVariable int userId){
//        List<Trip> trips=tripService.getAllTrips();
//
//        List<Trip> tripList = trips.stream()
//                .filter(trip -> trip.getUser().stream()
//                        .anyMatch(user -> user.getId().equals(userId))) // проверяем, есть ли пользователь в поездке
//                .collect(Collectors.toList());
//
//        Optional<List<User>> usersList=userService.findListUserById(tripList);
//        if (usersList.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users not found for provided trip IDs");
//        }
//        User user=usersList.get().stream().filter(user1 -> user1.getId().equals(userId)).findAny().get();
//        return ResponseEntity.ok(user);
//        List<Trip> trips = tripService.getAllTrips();
//
//         Фильтруем поездки, в которых участвует данный пользователь
//        List<Trip> tripList = trips.stream()
//                .filter(trip -> trip.getUser().stream()
//                        .anyMatch(user -> user.getId().equals(userId))) // проверяем, есть ли пользователь в поездке
//                .collect(Collectors.toList());
//
//       return ResponseEntity.ok(trips);
//    }
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

