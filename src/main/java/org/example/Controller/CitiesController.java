package org.example.Controller;

import org.example.Service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/cities")
@RestController
public class CitiesController {
    @Autowired
    private final CitiesService citiesService;

    public CitiesController(CitiesService citiesService) {
        this.citiesService = citiesService;
    }
    @GetMapping("/getAllCities")
    public ResponseEntity<?> getAllCities() {
    return ResponseEntity.ok(citiesService.getCities());
    }
}
