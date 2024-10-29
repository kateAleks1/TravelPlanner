package org.example.Controller;

import org.example.Service.StatusTripService;
import org.example.entity.Trip_Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/trip_status")
public class StatusTripController {
    @Autowired
    private final StatusTripService statusTripService;

    public StatusTripController(StatusTripService statusTripService) {
        this.statusTripService = statusTripService;
    }

    @GetMapping("/getStatusTrip")
    public ResponseEntity<?> getStatusTripTable( ){
        List<Trip_Status> list=statusTripService.getAllTrips();
        return ResponseEntity.ok(list);
    }
}
