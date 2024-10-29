package org.example.Dal.Repository;

import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {


Optional<Trip> findById(Integer id);
    Optional<List<Trip>> findByUserId(Integer userId);


}
