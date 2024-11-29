package org.example.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.example.Dal.Repository.DestinationsRepository;
import org.example.Dal.Repository.TripRepository;
import org.example.Service.TripDestinationService;
import org.example.entity.Destination;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TripDestinationServiceImpl implements TripDestinationService {

    private TripRepository tripRepository;
    private DestinationsRepository destinationRepository;

    @Autowired
    public TripDestinationServiceImpl(TripRepository tripRepository, DestinationsRepository destinationRepository) {
        this.tripRepository = tripRepository;
        this.destinationRepository = destinationRepository;
    }

    @Override
    public void addTripDestination(int tripId, int destinationId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found"));

        if (!trip.getDestinations().contains(destination)) {
            trip.getDestinations().add(destination);
            tripRepository.save(trip); // Hibernate обновит промежуточную таблицу
        }
    }

    @Override
    public void removeTripDestination(int tripId, int destinationId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found"));

        if (trip.getDestinations().contains(destination)) {
            trip.getDestinations().remove(destination);
            tripRepository.save(trip); // Hibernate обновит промежуточную таблицу
        }
    }

    @Override
    public boolean isDestinationAdded(int tripId, int destinationId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found"));

        return trip.getDestinations().contains(destination);
    }
}


