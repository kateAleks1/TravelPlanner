package org.example.Service.ServiceImpl;

import org.example.Dal.Repository.TripStatusRepository;
import org.example.Service.StatusTripService;
import org.example.entity.Trip_Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StatusTripServiceImpl implements StatusTripService {


    @Autowired
    private final TripStatusRepository tripStatusRepository;

    public StatusTripServiceImpl(TripStatusRepository tripStatusRepository) {
        this.tripStatusRepository = tripStatusRepository;
    }

    @Override
    public Trip_Status findStatusTripById(int tripId) {
        return tripStatusRepository.findStatusByTripId(tripId).get();
    }
    @Override
    public List<Trip_Status> getAllTrips() {
        return tripStatusRepository.findAll();
    }
}
