package org.example.Controller;

import org.example.DTO.TripDestinationRequest;
import org.example.Service.TripDestinationService;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tripDestinations")
public class TripDestinationController {

    private TripDestinationService tripDestinationService;
    @Autowired
    public TripDestinationController(TripDestinationService tripDestinationService) {
        this.tripDestinationService = tripDestinationService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addTripDestination(@RequestBody TripDestinationRequest request) {
        tripDestinationService.addTripDestination(request.getTripId(), request.getDestinationId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeTripDestination(@RequestBody TripDestinationRequest request) {
        tripDestinationService.removeTripDestination(request.getTripId(), request.getDestinationId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isAdded/{tripId}/{destinationId}")
    public ResponseEntity<Boolean> isDestinationAdded(@PathVariable int tripId, @PathVariable int destinationId) {
        boolean isAdded = tripDestinationService.isDestinationAdded(tripId, destinationId);
        return ResponseEntity.ok(isAdded);
    }
}
