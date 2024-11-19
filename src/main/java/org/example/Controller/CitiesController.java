package org.example.Controller;

import org.example.Service.CitiesService;
import org.example.entity.Cities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @GetMapping("/getCitiesByCountryName/{countryName}")
    public ResponseEntity<?> getAllCitiesByCountryName(@PathVariable String countryName){
       return ResponseEntity.ok(citiesService.getCitiesByCountryName(countryName));
    }
    @GetMapping("/getCityById/{cityId}")
    public ResponseEntity<?> getCityNameById(@PathVariable int cityId) {
        Optional<Cities> city = citiesService.getCitiesById(cityId);
        if (city.isPresent()) {
            return ResponseEntity.ok(city.get());  // Возвращаем найденный город
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("City with ID " + cityId + " not found");
        }
    }
}
