package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {

    @Query("SELECT t.destinations FROM Trip t WHERE t.tripId = :tripId")
    Optional<List<Destination>> findDestinationsByTripId(@Param("tripId") int tripId);




    @Query("SELECT t FROM Trip t WHERE t.startDate >= :startDate AND t.endDate >= :endDate")
    List<Trip> findTripsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM trip_destinations WHERE trip_id = :tripId AND destination_id = :destinationId", nativeQuery = true)
    void deleteDestinationFromTrip(@Param("tripId") int tripId, @Param("destinationId") int destinationId);

    Optional<Set<Trip>> findByParticipants_User_Id(Integer id);
    @Query("select t.city.cityId from Trip t where t.tripId=:tripId")
    Optional<Integer> getCityFromTripById(@Param("tripId") int tripID);

   // select trips.* from trips inner JOIN cities c on c.city_id=trips.city_id
   // join travels.countries c2 on c2.country_id = c.country_id where country_name='Austria'
   @Transactional
   @Query("SELECT t FROM Trip t " +
           "JOIN t.city c " +
           "JOIN c.countryId co " +
           "JOIN t.participants tp " +
           "WHERE co.countryName = :countryName AND tp.user.id = :userId")
   List<Trip> findTripsByUserIdAndCountryName(@Param("userId") int userId, @Param("countryName") String countryName);

    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.user.id = :userId")
    Optional<List<Trip>> getTripByUsersId(@Param("userId") int userId);
    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.user.login = :userLogin")
    Optional<List<Trip>> getTripByUsersLogin(@Param("userLogin") String userLogin);
    @Query("SELECT t FROM Trip t JOIN FETCH t.participants")
    List<Trip> findAllWithParticipants();
    @Modifying
    @Query("DELETE FROM TripPartcipants tp WHERE tp.user.id = :userId AND tp.trip.tripId = :tripId")
    void deleteByUserIdAndTripId(@Param("userId") int userId, @Param("tripId") int tripId);
    @Modifying
    @Transactional
    void deleteByTripId(int tripId);
    @Query("SELECT t FROM Trip t JOIN t.city c WHERE c.cityName LIKE %:prefix%")
    Optional<List<Trip>> findTripsByPrefix(@Param("prefix")String prefix);
}


