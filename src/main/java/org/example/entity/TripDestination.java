package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "trip_destinations")
public class TripDestination {
    @EmbeddedId
    private TripDestinationId id;
    @ManyToOne
    @MapsId("tripId")
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @MapsId("destinationId")
    @JoinColumn(name = "destination_id")
    private Destination destination;
}
