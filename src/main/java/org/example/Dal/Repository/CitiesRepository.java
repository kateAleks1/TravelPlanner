package org.example.Dal.Repository;

import org.example.entity.Cities;
import org.example.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Integer> {
    Optional<Cities> findByCityName(String cityName);

    @Query("SELECT c FROM Cities c WHERE c.countryId.countryId = :id")
    Optional<List<Cities>> findAllCitiesFromCountryId(@Param("id") int id);


}
