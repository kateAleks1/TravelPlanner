package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.example.entity.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DestinationsRepository extends JpaRepository<Destination,Integer> {

    Optional<Destination> findByDestinationId(int integer);

    @Query("SELECT d FROM  Destination d where d.destinationType.typeName=:typeName")
    Optional<List<Destination>> findDestinationByDestinationType(@Param("typeName") String typeName);
    @Query("SELECT d FROM  Destination d where d.destinationType.typeName=:typeName and d.cities.cityId=:cityId")
Optional<List<Destination>> findDestinationByCityAndDByDestinationType(@Param("typeName") String typeName,@Param("cityId") int cityId);
    Optional<Destination> findDestinationByName(String cityName);
    @Query("FROM Destination d WHERE d.cities.cityId = :cityId")
    Optional<List<Destination>> findAllDestinationsByCityId(@Param("cityId")int city_id);

    @Query("SELECT d FROM Destination d JOIN d.trips t WHERE d.destinationId = :id")
Optional<List<Destination>> findDestinationByTripsAndDestinationId(@Param("id") int destinationId);

    @Query("SELECT d FROM Trip t JOIN t.destinations d WHERE t.tripId = :tripId AND d.destinationType.typeName = :typeName")
    List<Destination> findDestinationsByTypeByTripId(@Param("tripId") int tripId, @Param("typeName") String typeName);

    @Query("SELECT d FROM Destination d JOIN d.cities c WHERE c.cityId=:cityId and d.name LIKE %:prefix%")
    Optional<List<Destination>> findDestinationByPrefix(@Param("cityId") int cityId,@Param("prefix")String prefix);



}
