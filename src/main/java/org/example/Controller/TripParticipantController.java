package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Service.TripParticipantService;
import org.example.entity.TripPartcipants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip-participants")
@RequiredArgsConstructor
public class TripParticipantController {

    private final TripParticipantService tripParticipantService;

    @GetMapping("/users/{tripId}")
    public ResponseEntity<?> getUsersByTripId(@PathVariable int tripId) {
        List<Integer> userIds = tripParticipantService.getUserIdsByTripId(tripId);
        if (userIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found for the provided trip ID.");
        }
        return ResponseEntity.ok(userIds);
    }

    @DeleteMapping("/{tripId}/{userId}")
    public ResponseEntity<?> removeUserFromTrip(@PathVariable int tripId, @PathVariable int userId) {
        tripParticipantService.removeUserFromTrip(tripId, userId);
        return ResponseEntity.ok("User removed from trip.");
    }
    @GetMapping("/getAllTripParticipants")
    public ResponseEntity<?> getAllTripParticipants() {
List<TripPartcipants> list = tripParticipantService.getAllTripParticipants();
        return ResponseEntity.ok(list);
    }
}
