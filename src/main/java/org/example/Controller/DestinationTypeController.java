package org.example.Controller;

import org.example.DTO.DestinationTypeDto;
import org.example.Service.DestinationTypeService;
import org.example.entity.DestinationType;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/destinationType")
@RestController
public class DestinationTypeController {
    private DestinationTypeService destinationTypeService;
@Autowired
    public DestinationTypeController(DestinationTypeService destinationTypeService) {
        this.destinationTypeService = destinationTypeService;
    }

    @GetMapping("/getDestinationTypeById/{destinationId}")
    public ResponseEntity<?> getDestinationTypeById(@PathVariable  Integer destinationId){
        DestinationType destinationType= destinationTypeService.findDestinationTypeByTypeId(destinationId);
  return ResponseEntity.ok(destinationType);
    }

    @GetMapping("/getAllDestinationsType")
    public ResponseEntity<?> getAllDestinationsType() {
        return ResponseEntity.ok(destinationTypeService.findAllDestinationsType());
    }
}
