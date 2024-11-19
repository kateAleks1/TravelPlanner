package org.example.Controller;

import org.example.Service.CitiesService;
import org.example.Service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
