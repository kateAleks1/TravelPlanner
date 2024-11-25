package org.example.Dal.Repository;

import org.example.entity.Trip;
import org.example.entity.TripPartcipants;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripParticipantRepository extends JpaRepository<TripPartcipants, Integer> {

    // Получить список пользователей для определённой поездки
    @Query("SELECT tp.user.id FROM TripPartcipants tp WHERE tp.trip.tripId = :tripId")
    List<Integer> findUserIdsByTripId(@Param("tripId") int tripId);

    // Получить список поездок для определённого пользователя
    @Query(value = "SELECT trip_id FROM trip_participants WHERE user_id = :userId", nativeQuery = true)
    List<Integer> findTripIdsByUserId(@Param("userId") int userId);
    Optional<TripPartcipants> findByUserAndTrip(User user, Trip trip);
    @Query("SELECT tp FROM TripPartcipants tp WHERE tp.trip = :trip AND tp.isOrganizer = :isOrganizer")
    Optional<TripPartcipants> findByTripAndIsOrganizer(@Param("trip") Trip trip, @Param("isOrganizer") boolean isOrganizer);

    // Удалить связь между поездкой и пользователем
    @Modifying
    @Query(value = "DELETE FROM trip_participants WHERE trip_id = :tripId AND user_id = :userId", nativeQuery = true)
    void deleteUserFromTrip(@Param("tripId") int tripId, @Param("userId") int userId);
    boolean existsByTripAndUser(Trip trip, User user);

    List<TripPartcipants> findAllByUser(User user);

    @Modifying
    @Transactional
    void deleteAllByTrip(Trip trip);

    @Modifying
    @Transactional
    void deleteAllByUser(User user);

}