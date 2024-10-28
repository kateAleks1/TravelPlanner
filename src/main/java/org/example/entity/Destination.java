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
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer destinationId;

    private String name;
    private String country;
    private String city;
    private String description;

    @ManyToMany(mappedBy = "destinations", cascade = CascadeType.ALL)//// Указывает на связь с Trip
    private List<Trip> trips;
}
