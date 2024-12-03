package org.example.Service.ServiceImpl;

import org.example.DTO.TripDto;
import org.example.Dal.Repository.DestinationsRepository;
import org.example.Service.DestinationService;
import org.example.entity.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {
    private final DestinationsRepository destinationsRepository;

@Autowired
    public DestinationServiceImpl(DestinationsRepository destinationsRepository) {
        this.destinationsRepository = destinationsRepository;
    }

    @Override
    public List<Destination> getAllDestinations() {
        return destinationsRepository.findAll();
    }

    @Override
    public List<Destination> getDestinationsFromUserId(int userId) {
        return List.of();
    }

    @Override
    public void createDestination(TripDto tripDto) {

    }

    @Override
    public Destination getAllDestinationById(int destinationId) {
        return destinationsRepository.findByDestinationId(destinationId).get();
    }

    @Override
    public List<Destination> getAllDestinationByName(String name) {
        return List.of();
    }

    @Override
    public List<Destination> findDestinationsByTypeByTripId(int tripId,String typeName) {
        return destinationsRepository.findDestinationsByTypeByTripId(tripId,typeName);
    }

    @Override
    public List<Destination> getAllDestinationByCityId(int cityId) {
        return destinationsRepository.findAllDestinationsByCityId(cityId).get();
    }

    @Override
    public List<Destination> findDestinationByPrefix(int cityId,String prefix) {
        return destinationsRepository.findDestinationByPrefix(cityId,prefix).get();
    }

    @Override
    public List<Destination> findDestinationByDestinationType( String typeName){
    return  destinationsRepository.findDestinationByDestinationType(typeName).get();
    }

    @Override
    public List<Destination> findDestinationByCityAndDByDestinationType(String typeName, int cityId) {
        return destinationsRepository.findDestinationByCityAndDByDestinationType(typeName,cityId).get();
    }
}
