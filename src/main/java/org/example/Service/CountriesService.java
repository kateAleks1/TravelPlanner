package org.example.Service;

import org.example.entity.Countries;
import org.example.entity.Trip;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CountriesService{
    List<Countries> getAllCountries();
    Optional<Integer> getCountryIdByCountryName(String countryName);
    Optional<Countries> getCountryCountryId(int countryId);
    boolean ifCountryExistsWithSuchId(int CountryId);
    Optional<String> getCountryNameByCountryId(int CountryId);
}
