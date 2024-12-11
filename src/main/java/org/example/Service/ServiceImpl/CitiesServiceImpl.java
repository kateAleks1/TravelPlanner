package org.example.Service.ServiceImpl;

import jakarta.persistence.EntityManager;
import org.example.DTO.CityStatistic;
import org.example.Dal.Repository.CitiesRepository;
import org.example.Dal.Repository.CountriesRepository;
import org.example.Service.CitiesService;
import org.example.entity.Cities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;
    private final CountryServiceImpl countryService;
    private final CountriesRepository countriesRepository;

    private final EntityManager entityManager;
@Autowired
    public CitiesServiceImpl(CitiesRepository citiesRepository, CountryServiceImpl countryService, CountriesRepository countriesRepository, EntityManager entityManager) {
        this.citiesRepository = citiesRepository;
        this.countryService = countryService;
        this.countriesRepository = countriesRepository;
        this.entityManager = entityManager;
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
    public List<CityStatistic> findTopThreeCities() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "cityCount"));
        Page<Object[]> results = citiesRepository.findTopThreeCities(pageable);

        return results.stream()
                .map(result -> new CityStatistic(
                        (String) result[0],
                        ((Number) result[1]).intValue()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<List<Cities>> getCitiesByCountryId(int id) {

        return Optional.empty();
    }
}
