package org.example.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.DTO.TripDto;
import org.example.DTO.UserDto;
import org.example.Service.ServiceImpl.TripServiceImpl;
import org.example.Service.TripService;
import org.example.entity.Trip;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trip")
public class TripController {
@Autowired
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/createNewTrip")
    public ResponseEntity<?> createNewTrip(@RequestBody TripDto tripDto) {
        try {
            tripService.createTrip(tripDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Поездка успешно создана"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Произошла ошибка: " + e.getMessage()));
        }
    }
    @GetMapping("/getAllTrips")
    public ResponseEntity<?> getAllTrips() {
            List<Trip> trips = tripService.getAllTrips();
            return ResponseEntity.ok(trips);

    }


}
