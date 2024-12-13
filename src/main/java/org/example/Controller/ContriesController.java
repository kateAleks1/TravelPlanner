package org.example.Controller;

import org.example.Service.CountriesService;
import org.example.Service.ServiceImpl.CountryServiceImpl;
import org.example.entity.Countries;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @GetMapping("/mostCommonCountriesBySelectedPeriod")
    public ResponseEntity<?> mostCommonCountriesByPeriod(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate) {

        return ResponseEntity.ok( countryService.mostCommonCountriesByUserSelectedPeriod(startDate));

    }

    @GetMapping("/mostCommonCountriesByAllTheTime")
    public ResponseEntity<?> mostCommonCountriesByAllTheTime() {
        Pageable pageable = PageRequest.of(0, 9, Sort.by("countryCount").descending());
        return ResponseEntity.ok( countryService.mostCommonCountriesByAllTheTime(pageable));

    }
}
