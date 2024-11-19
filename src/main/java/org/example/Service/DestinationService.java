package org.example.Service;


import org.example.DTO.TripDto;
import org.example.entity.Countries;
import org.example.entity.Destination;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinationService {
    List<Destination> getAllDestinations();
Destination getAllDestinationById(int destinationId);
List<Destination> getAllDestinationByCityId(int cityId);
List<Destination> getDestinationsFromUserId(int userId);
    void createDestination(TripDto tripDto);
List<Destination> getAllDestinationByName(String name);

}
