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
    public Optional<Integer> getCountryIdByCountryName(String countryName) {
        int countryId=0;
        if(countriesRepository.getCountryIdByCountryName(countryName).isPresent()){
           countryId=countriesRepository.getCountryIdByCountryName(countryName).get();
        }
        return Optional.of(countryId);
    }

    @Override
    public Optional<Countries> getCountryCountryId(int countryId) {
        if(countriesRepository.findByCountryId(countryId).isPresent()){
            Countries countries=countriesRepository.findByCountryId(countryId).get();
            return Optional.of(countries);
        }
       return Optional.empty();
    }

    @Override
    public boolean ifCountryExistsWithSuchId(int CountryId) {
        return false;
    }

    @Override
    public Optional<String> getCountryNameByCountryId(int CountryId) {
        // получение названия страны по id
        String countryName = "";
     if(countriesRepository.findByCountryId(CountryId).isPresent()){
         Countries countries=countriesRepository.findByCountryId(CountryId).get();
         countryName= countries.getCountryName();
     }
      return Optional.of(countryName);
    }
}
