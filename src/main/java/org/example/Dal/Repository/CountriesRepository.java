package org.example.Dal.Repository;

import org.example.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Optional<Countries> findByCountryName(String name);
    Optional<Countries> findByCountryId(int id);
    @Query("SELECT c.countryId FROM Countries c WHERE c.countryName = :name")
    Optional<Integer> getCountryIdByCountryName(@Param("name") String name);

}
