package org.example.Dal.Repository;

import org.example.entity.Trip_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripStatusRepository extends JpaRepository<Trip_Status,Integer> {

    @Query("SELECT t.statusTrip FROM Trip t WHERE t.tripId = :tripId")
    Optional<Trip_Status> findStatusByTripId(@Param("tripId") int tripId);
}
