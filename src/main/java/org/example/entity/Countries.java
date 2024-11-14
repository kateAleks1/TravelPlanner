package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @OneToMany(mappedBy = "countryId", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private  List<Cities> cities;

}