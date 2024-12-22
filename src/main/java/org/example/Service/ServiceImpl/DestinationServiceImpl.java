package org.example.Service.ServiceImpl;

import jakarta.transaction.Transactional;
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
    @Transactional
    @Override
    public void deleteDestinationImageUrlByDestinationId(int destinationId) {

        destinationsRepository.deleteDestinationImageUrlByDestinationId(destinationId);


    }
    @Override
    public void saveDestination(Destination destination) {
        // Проверяем, если destination существует, обновляем, если нет, сохраняем новый
        if (destination.getDestinationId() != null) {
            destinationsRepository.save(destination); // Обновляем или создаем новую запись
        } else {
            destinationsRepository.save(destination);
        }
    }
    @Override
    public void updateImageUrl(int destinationId, String imageUrl) {
        destinationsRepository.updateImageUrl(destinationId, imageUrl);
    }

    @Override
    public Destination setDestinationImageByDestinationId(String imageUrl, int destinationId) {
        if(destinationsRepository.findByDestinationId(destinationId).isPresent()){
           return destinationsRepository.setDestinationImageByDestinationId(imageUrl,destinationId).get();
        }
        return null;
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
