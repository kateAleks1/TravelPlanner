package org.example.Dal.Repository;

import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {



    Optional<Set<Trip>> findByUsers_Id(Integer id);


}
