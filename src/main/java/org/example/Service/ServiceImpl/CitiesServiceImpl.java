package org.example.Service.ServiceImpl;

import org.example.Dal.Repository.CitiesRepository;
import org.example.Dal.Repository.CountriesRepository;
import org.example.Service.CitiesService;
import org.example.entity.Cities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;
    private final CountryServiceImpl countryService;
    private final CountriesRepository countriesRepository;
@Autowired
    public CitiesServiceImpl(CitiesRepository citiesRepository, CountryServiceImpl countryService, CountriesRepository countriesRepository) {
        this.citiesRepository = citiesRepository;
        this.countryService = countryService;
        this.countriesRepository = countriesRepository;
    }

    @Override
    public List<Cities> getCities() {
        return citiesRepository.findAll();
    }

    @Override
    public Optional<Cities> getCitiesById(int id) {
        Optional<Cities> city = citiesRepository.findById(id);
        return city;  // Возвращаем Optional, не вызывая .get()
    }


    public Optional<List<Cities>> getCitiesByCountryName(String countryName) {
        Optional<Integer> countryIdOpt = countriesRepository.getCountryIdByCountryName(countryName);
        if (countryIdOpt.isPresent()) {
            int countryId = countryIdOpt.get();
            return citiesRepository.findAllCitiesFromCountryId(countryId);
        }
        return Optional.empty();
    }
    @Override
    public Optional<List<Cities>> getCitiesByCountryId(int id) {

        return Optional.empty();
    }
}
