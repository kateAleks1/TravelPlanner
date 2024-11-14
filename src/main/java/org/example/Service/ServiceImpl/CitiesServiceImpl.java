package org.example.Service.ServiceImpl;

import org.example.Dal.Repository.CitiesRepository;
import org.example.Service.CitiesService;
import org.example.entity.Cities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;
    @Autowired
    public CitiesServiceImpl(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    @Override
    public List<Cities> getCities() {
        return citiesRepository.findAll();
    }

    @Override
    public Optional<Cities> getCitiesById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Cities>> getCitiesByCountryId(int id) {
        return Optional.empty();
    }
}
