package org.example.Dal.Repository;

import org.example.DTO.CityStatistic;
import org.example.DTO.DestinationsStatistic;
import org.example.DTO.TripSortingCountries;
import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT tp.trip.tripId as tripId, COUNT(tp.trip.tripId) as tripCount " +
            "FROM TripPartcipants tp " +
            "GROUP BY tp.trip.tripId ")
    List<Object[]> findMostFrequentTrips();

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
    @Query("select t.createdAt from Trip t where t.tripId=:tripId")
Date getCreatedAtFromTripById(@Param("tripId") int tripID);
    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.user.id = :userId")
    Optional<List<Trip>> getTripByUsersId(@Param("userId") int userId);

    @Query("SELECT c.imageUrl FROM TripPartcipants tp " +
            "JOIN tp.trip t " +
            "JOIN t.city c " +
            "WHERE tp.user.id = :userId " +
            "AND tp.trip.tripId = :tripId")
    Optional<String> findAllTripBackgroundImagesByCityId(@Param("userId") int userId, @Param("tripId") int tripId);


    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.user.login = :userLogin")
    Optional<List<Trip>> getTripByUsersLogin(@Param("userLogin") String userLogin);
    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.isGroup=true AND tp.user.id=:userid")
    Optional<List<Trip>> getTripByGroup(@Param("userid") int userId);
    @Query("SELECT t FROM Trip t JOIN TripPartcipants tp ON tp.trip = t WHERE tp.isGroup=false AND tp.user.id=:userid")
    Optional<List<Trip>> getAloneTrip(@Param("userid") int userId);
    @Query("SELECT t FROM Trip t JOIN FETCH t.participants")
    List<Trip> findAllWithParticipants();
    @Modifying
    @Query("DELETE FROM TripPartcipants tp WHERE tp.user.id = :userId AND tp.trip.tripId = :tripId")
    void deleteByUserIdAndTripId(@Param("userId") int userId, @Param("tripId") int tripId);
    @Modifying
    @Transactional
    void deleteByTripId(int tripId);
    @Query("SELECT t FROM Trip t JOIN t.participants tp JOIN t.city c WHERE tp.user.id = :userId AND c.cityName LIKE %:prefix%")
    Optional<List<Trip>> findTripsByPrefix(@Param("userId") int userId, @Param("prefix") String prefix);


    @Query("SELECT COUNT(tp.destination.destinationId) AS countDestinations, " +
            "tp.destination.name " +
            "FROM TripDestination tp " +
            "GROUP BY tp.destination.destinationId, tp.destination.name")
    Page<Object[]> findMostCommonDestination(Pageable pageable);

    @Query("SELECT new org.example.DTO.DestinationsStatistic(tp.destination.name, COUNT(tp.destination.destinationId)) " +
            "FROM TripDestination tp " +
            "GROUP BY tp.destination.destinationId, tp.destination.name")
    List<DestinationsStatistic> findAllMostCommonDestination();

    @Query("SELECT new org.example.DTO.TripSortingCountries(co.countryName, COUNT(tp),co.imageUrl) " +
            "FROM TripPartcipants tp " +
            "JOIN tp.trip t " +
            "JOIN t.city c " +
            "JOIN c.countryId co " +
            "GROUP BY co.countryName,co.imageUrl " +
            "ORDER BY COUNT(tp) DESC")
    List<TripSortingCountries> findAllMostCommonTripsByCountry();
    @Query("SELECT new org.example.DTO.TripSortingCountries(co.countryName, COUNT(tp),co.imageUrl) " +
            "FROM TripPartcipants tp " +
            "JOIN tp.trip t " +
            "JOIN t.city c " +
            "JOIN c.countryId co " +
            "GROUP BY co.countryName,co.imageUrl " +
            "ORDER BY COUNT(tp) ASC ")
    List<TripSortingCountries> findAllLeastCommonTripsByCountry();

}


