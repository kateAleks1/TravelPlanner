package org.example.Service;

import org.example.entity.Cities;

import java.util.List;
import java.util.Optional;

public interface CitiesService {
    List<Cities> getCities();
    Optional<Cities> getCitiesById(int id);
    Optional<List<Cities>> getCitiesByCountryId(int id);
}
