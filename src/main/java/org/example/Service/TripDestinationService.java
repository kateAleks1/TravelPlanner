package org.example.Service;

public interface TripDestinationService {
    void addTripDestination(int tripId, int destinationId);
    void removeTripDestination(int tripId, int destinationId);
    boolean isDestinationAdded(int tripId, int destinationId);

}
