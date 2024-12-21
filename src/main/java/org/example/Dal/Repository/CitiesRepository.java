package org.example.Dal.Repository;

import org.example.entity.Cities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT c.cityName AS cityName, COUNT(c.cityId) AS cityCount " +
            "FROM Trip t JOIN t.city c " +
            "GROUP BY c.cityId, c.cityName " +
            "ORDER BY cityCount DESC")
    Page<Object[]> findTopThreeCities(Pageable pageable);
    @Query("SELECT c.countryId.countryName FROM Cities c where c.cityId=:cityId")
String getCountryNamefromCityByCityName(@Param("cityId") int cityName);

}
