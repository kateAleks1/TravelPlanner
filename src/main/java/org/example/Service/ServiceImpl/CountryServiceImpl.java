package org.example.Service.ServiceImpl;

import org.example.DTO.CityStatistic;
import org.example.Dal.Repository.CountriesRepository;
import org.example.Service.CountriesService;
import org.example.entity.Countries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<CityStatistic> mostCommonCountriesByPeriod() {
        Pageable pageable= PageRequest.of(0,3);

        LocalDate localDate = LocalDate.of(2024, 11, 10);

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Page<Object[]> results = countriesRepository.mostCommonCountriesByPeriod(date,pageable);
        return results.stream().map(result -> new CityStatistic(
                (String) result[0],
                ((Number) result[1]).intValue()
        )).collect(Collectors.toList());
    }

    @Override
    public List<CityStatistic> mostCommonCountriesByAllTheTime(Pageable pageable) {
        // Передаем pageable в репозиторий
        Page<Object[]> results = countriesRepository.mostCommonCountriesByAllTheTime(pageable);

        return results.stream()
                .map(result -> new CityStatistic(
                        (String) result[0],  // Название страны
                        ((Number) result[1]).intValue()  // Количество стран
                ))
                .collect(Collectors.toList());
    }
}
