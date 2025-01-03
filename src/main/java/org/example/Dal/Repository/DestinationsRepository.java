package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DestinationsRepository extends JpaRepository<Destination,Integer> {

    Optional<Destination> findByDestinationId(int integer);

    @Query("SELECT t.review FROM Destination t WHERE t.destinationId = :destinationId")
    Optional<List<Review>> findReviewByDestinationId(@Param("destinationId") int destinationId);

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
    @Modifying
    @Query("UPDATE Destination d SET d.imageUrl = NULL WHERE d.destinationId = :destinationId")
    void deleteDestinationImageUrlByDestinationId(@Param("destinationId") int destinationId);
    @Modifying
    @Query("UPDATE Destination d SET d.imageUrl = :imageUrl WHERE d.destinationId = :destinationId")
    int updateImageUrl(@Param("destinationId") int destinationId, @Param("imageUrl") String imageUrl);

    @Query("SELECT d FROM Destination d JOIN d.cities c WHERE c.cityId=:cityId and d.name LIKE %:prefix%")
    Optional<List<Destination>> findDestinationByPrefix(@Param("cityId") int cityId,@Param("prefix")String prefix);
    @Query("update  Destination set imageUrl=:imageUrl WHERE destinationId = :destinationId")
    Optional<Destination> setDestinationImageByDestinationId(@Param("imageUrl") String imageUrl,@Param("destinationId") int destinationId);


}
