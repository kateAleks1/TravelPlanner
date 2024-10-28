package org.example.Dal.Repository;

import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {
    //repository

}
