package org.example.Dal.Repository;

import org.example.entity.Trip_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripStatusRepository extends JpaRepository<Trip_Status,Integer> {

    Optional<Trip_Status> findTrip_StatusByStatusId(int tripStatusId);
}
