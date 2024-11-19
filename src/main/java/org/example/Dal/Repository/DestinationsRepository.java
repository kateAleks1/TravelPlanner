package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DestinationsRepository extends JpaRepository<Destination,Integer> {

    Optional<Destination> findByDestinationId(int integer);


    Optional<Destination> findDestinationByName(String cityName);
    @Query("FROM Destination d WHERE d.cities.cityId = :cityId")
    Optional<List<Destination>> findAllDestinationsByCityId(@Param("cityId")int city_id);
    @Query("SELECT d FROM Destination d JOIN d.trips t WHERE d.destinationId = :id")
Optional<List<Destination>> findDestinationByTripsAndDestinationId(@Param("id") int destinationId);
}
