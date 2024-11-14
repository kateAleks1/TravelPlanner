package org.example.Dal.Repository;

import org.example.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Optional<Countries> findByCountryName(String name);
}
