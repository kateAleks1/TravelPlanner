package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "countries")
public class Countries {

    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer countryId;

    @Column(name = "country_name")
    private String countryName;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Destination> destinations;

}