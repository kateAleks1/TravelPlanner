package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class TripDestinationId implements Serializable {

    private int tripId;

    private int destinationId;

    // Getters, Setters, hashCode, equals
}
