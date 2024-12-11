package org.example.Controller;

import org.example.Service.CountriesService;
import org.example.Service.ServiceImpl.CountryServiceImpl;
import org.example.entity.Countries;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/api/countries")
@RestController
public class ContriesController {

    @Autowired
    private CountryServiceImpl countryService;

    public ContriesController(CountryServiceImpl countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/getAllCountries")
    public ResponseEntity<?> getAllCountries() {
        List<Countries> trips = countryService.getAllCountries();
        return ResponseEntity.ok(trips);

    }
    @GetMapping("/mostCommonCountriesByPeriod")
    public ResponseEntity<?> mostCommonCountriesByPeriod() {

        return ResponseEntity.ok( countryService.mostCommonCountriesByPeriod());

    }

    @GetMapping("/mostCommonCountriesByAllTheTime")
    public ResponseEntity<?> mostCommonCountriesByAllTheTime() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("countryCount").descending());
        return ResponseEntity.ok( countryService.mostCommonCountriesByAllTheTime(pageable));

    }
}
