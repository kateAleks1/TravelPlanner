package org.example.Controller;

import org.example.Service.CitiesService;
import org.example.Service.DestinationService;
import org.example.entity.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/destination")
@RestController
public class DestinationController {
    @Autowired
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/getAllDestination")
    public ResponseEntity<?> getAllDestinations(){
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }
    @GetMapping("/getAllDestinationsByCityId/{cityId}")
    public ResponseEntity<?> getAllDestinationsByCityId(@PathVariable int cityId){
        return ResponseEntity.ok(destinationService.getAllDestinationByCityId(cityId));
    }
    @GetMapping("/findDestinationsByTypeByTripId/{tripId}/{typeName}")
    public ResponseEntity<?> findDestinationsByTypeByTripId(@PathVariable int tripId,@PathVariable String typeName){
        return ResponseEntity.ok(destinationService.findDestinationsByTypeByTripId(tripId,typeName));
    }
    @GetMapping("/findDestinationByDestinationType/{typeName}")
    public ResponseEntity<?> findDestinationByDestinationType(@PathVariable String typeName){
        return ResponseEntity.ok(destinationService.findDestinationByDestinationType(typeName));
    }
    @GetMapping("/findDestinationByCityAndDByDestinationType/{typeName}/{cityId}")
    public ResponseEntity<?> findDestinationByCityAndDByDestinationType(@PathVariable String typeName,@PathVariable int cityId){
        return ResponseEntity.ok(destinationService.findDestinationByCityAndDByDestinationType(typeName,cityId));
    }

    @GetMapping("/findDestinationByPrefix/{cityId}")
    public ResponseEntity<?> findDestinationByPrefix(@PathVariable int cityId,@RequestParam String query){
        return ResponseEntity.ok(destinationService.findDestinationByPrefix(cityId,query));
    }

}
