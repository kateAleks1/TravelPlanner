package org.example.Service.ServiceImpl;

import org.example.Dal.Repository.CountriesRepository;
import org.example.Service.CountriesService;
import org.example.entity.Countries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountriesService {

    private final CountriesRepository countriesRepository;
    @Autowired
    public CountryServiceImpl(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    @Override
    public List<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

    @Override
    public Optional<Countries> getCountryIdByCountryName(String countryName) {
        return Optional.empty();
    }

    @Override
    public Optional<Countries> getCountryNamebyCountryId(int countryId) {
        return Optional.empty();
    }

    @Override
    public boolean ifCountryExistsWithSuchId(int CountryId) {
        return false;
    }
}
