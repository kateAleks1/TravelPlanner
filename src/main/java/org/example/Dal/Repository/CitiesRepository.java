package org.example.Dal.Repository;

import org.example.entity.Cities;
import org.example.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Integer> {
    Optional<Cities> findByCityName(String cityName);


}
