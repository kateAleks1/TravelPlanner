package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cities")
public class Cities {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="city_id")
    @Id
    private int cityId;
    @Column(name="city_name", nullable = false,unique = true)
    private String cityName;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Countries countryId;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "trip_cities",
            joinColumns = @JoinColumn(name = "city_id"),
            inverseJoinColumns = @JoinColumn(name = "trip_id"))
    private Set<Trip> trips = new HashSet<>();
}
