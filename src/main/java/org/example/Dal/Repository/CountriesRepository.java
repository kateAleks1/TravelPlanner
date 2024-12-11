package org.example.Dal.Repository;

import org.example.entity.Countries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Optional<Countries> findByCountryName(String name);
    Optional<Countries> findByCountryId(int id);
    @Query("SELECT c.countryId FROM Countries c WHERE c.countryName = :name")
    Optional<Integer> getCountryIdByCountryName(@Param("name") String name);
    @Query("SELECT co.countryName AS countryName, COUNT(c.countryId) AS countryCount " +
            "FROM Trip t JOIN t.city c JOIN Countries co " +
            "GROUP BY co.countryName ")
    Page<Object[]> mostCommonCountriesByPeriod(Pageable pageable);
    @Query("SELECT co.countryName AS countryName, COUNT(co.countryId) AS countryCount " +
            "FROM Trip t " +
            "JOIN t.city c " +
            "JOIN c.countryId co " +
            "WHERE t.createdAt >= :startDate " +
            "GROUP BY co.countryName " +
            "ORDER BY countryCount DESC")
    Page<Object[]> mostCommonCountriesByPeriod(@Param("startDate") Date startDate, Pageable pageable);
    @Query("SELECT co.countryName AS countryName, COUNT( co) AS countryCount " +
            "FROM Trip t " +
            "JOIN t.city c " +
            "JOIN c.countryId co " +
            "GROUP BY co.countryName")
    Page<Object[]> mostCommonCountriesByAllTheTime(Pageable pageable);



}
